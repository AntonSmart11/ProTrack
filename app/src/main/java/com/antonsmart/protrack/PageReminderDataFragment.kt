package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import com.antonsmart.protrack.global.Global
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageReminderDataBinding
import com.antonsmart.protrack.objects.Reminder
import com.google.android.material.textfield.TextInputLayout
import java.sql.SQLData
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PageReminderDataFragment : Fragment(R.layout.fragment_page_reminder_data) {
    private lateinit var binding: FragmentPageReminderDataBinding
    private val args: PageReminderDataFragmentArgs by navArgs()
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var autoCompleteWork: AutoCompleteTextView
    private lateinit var adapterItemsWork: ArrayAdapter<String>

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageReminderDataBinding.bind(view)

        sqLiteHelper = SQLiteHelper(requireContext())

        val reminderId = args.idReminder

        val reminder = getReminder(reminderId)

        //Content
        val titlePage = binding.titleReminderPageData
        val titleReminder = binding.titleReminder
        val work = binding.workReminder
        val date = binding.dateReminder

        updatePage(reminder,titlePage,titleReminder,work, date)

        //Buttons
        val btnEdit = binding.editReminder
        val btnDelete = binding.deleteReminder

        btnEdit.setOnClickListener {
            showEditReminderDialog(reminderId,titleReminder,work,date)
        }

        btnDelete.setOnClickListener {
            DeleteReminder(reminderId, view)
        }

        binding.arrowBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun DeleteReminder(id: Int, view: View){
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.delete_reminder_alert_dialog,null)
        dialog.setContentView(dialogLayout)

        val nextButton = dialog.findViewById<Button>(R.id.btnNextDeleteReminder)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelDeleteReminder)

        nextButton.setOnClickListener {
            sqLiteHelper.DeleteReminder(id)
            dialog.dismiss()
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditReminderDialog(id: Int, title1: TextView, work1: TextView, date1: TextView){

        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.reminder_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val id_project = Global.idProject
        val titleEditText = dialogLayout.findViewById<EditText>(R.id.titleReminder)
        val workTextInputLayout = dialogLayout.findViewById<TextInputLayout>(R.id.reminderTextInputLayout)
        val dateEditText = dialogLayout.findViewById<EditText>(R.id.dateReminderEditText)

        val works = getWorks(id_project)

        autoCompleteWork = dialogLayout.findViewById(R.id.list_reminder)

        dateEditText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), R.style.DatePickerStyle, {_, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
                dateEditText.setText(formattedDate)
            }, year, month, day)

            //Set min date
            val minDateParts = minDate(id_project)
            val minParts = minDateParts.split("-")
            val dayMinPart = minParts[0]
            val monthMinPart = minParts[1]
            val yearMinPart = minParts[2]

            val calendarMin = Calendar.getInstance()
            calendarMin.set(yearMinPart.toInt(), monthMinPart.toInt() - 1, dayMinPart.toInt())
            val minDate = calendarMin.timeInMillis
            datePickerDialog.datePicker.minDate = minDate

            datePickerDialog.show()

            datePickerDialog.show()

            //Set max date
            val maxDateParts = maxDate(id_project)
            val maxParts = maxDateParts.split("-")
            val dayMaxPart = maxParts[0]
            val monthMaxPart = maxParts[1]
            val yearMaxPart = maxParts[2]

            val calendarMax = Calendar.getInstance()
            calendarMax.set(yearMaxPart.toInt(), monthMaxPart.toInt() - 1, dayMaxPart.toInt())
            val maxDate = calendarMax.timeInMillis
            datePickerDialog.datePicker.maxDate = maxDate

            datePickerDialog.show()

        }

        val reminder = getReminder(id)

        titleEditText.setText(reminder.title)
        dateEditText.setText(reminder.date)

        autoCompleteWork.setText(reminder.work)
        autoCompleteWork.setSelection(reminder.work.length)

        adapterItemsWork = ArrayAdapter<String>(requireContext(), R.layout.list_item_work, works)

        autoCompleteWork.setAdapter(adapterItemsWork)

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextReminderButton)
        nextButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val date = dateEditText.text.toString()
            val workTextInputEditText = workTextInputLayout.editText
            val work = workTextInputEditText?.text?.toString()

            if (!title.isEmpty()){
                if (!work!!.isEmpty()){
                    if (!date.isEmpty()) {
                        val reminder = Reminder(id,title,Global.idUser,Global.idProject,work, date)
                        val status = sqLiteHelper.UpdateReminder(reminder)

                        if (status > -1) {
                            Toast.makeText(requireContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()

                            updatePage(reminder,title1,title1,work1,date1)
                        }
                    } else {
                        Toast.makeText(requireContext(), "Campo fecha vacío", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(requireContext(), "No hay una tarea seleccionada", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(requireContext(), "Campo titulo vacío", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun updatePage(reminder: Reminder, title: TextView,title_reminder : TextView, work: TextView, date: TextView){
        title.setText(reminder.title)
        title_reminder.setText(reminder.title)
        work.setText(reminder.work)
        date.setText(reminder.date)
    }

    private fun getReminder(id: Int): Reminder{
        val reminderList = sqLiteHelper.GetAllReminders()
        val reminder = reminderList.find { it.id == id }

        return reminder!!
    }

    private fun getWorks (id: Int): ArrayList<String> {
        val workList = sqLiteHelper.GetAllWorks()
        val works = workList.filter { it.id_project == id }
        var listWorks: ArrayList<String> = ArrayList()

        listWorks.clear()

        for (work in works){
            listWorks.add(work.title)
        }

        return  listWorks
    }

    private fun minDate(id: Int): String {
        val projectList = sqLiteHelper.GetAllProjects()
        val project = projectList.find { it.id == id }

        val date_start = project!!.date_start

        return date_start
    }

    private fun maxDate(id: Int): String {
        val projectList = sqLiteHelper.GetAllProjects()
        val project = projectList.find { it.id == id }

        val date_end = project!!.date_end

        return date_end
    }
}