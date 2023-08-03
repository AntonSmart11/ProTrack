package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.ReminderAdapter
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageReminderBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Project
import com.antonsmart.protrack.objects.Reminder
import com.google.android.material.textfield.TextInputLayout
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class PageReminderFragment : Fragment(R.layout.fragment_page_reminder) {

    private lateinit var binding: FragmentPageReminderBinding
    private var listReminders: MutableList<Reminder> = mutableListOf()
    private var listProjects: MutableList<Project> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private lateinit var sqLiteHelper: SQLiteHelper
    private lateinit var autoCompleteWork: AutoCompleteTextView
    private lateinit var adapterItemsWork: ArrayAdapter<String>


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqLiteHelper = SQLiteHelper(requireContext())


        getReminders(Global.idProject)

        setAdapter()

        binding.addReminders.setOnClickListener {
            showCreateReminderDialog()
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


    private fun setAdapter() {
        recycler = requireView().findViewById(R.id.page_reminderRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ReminderAdapter(requireContext(), listReminders)
    }

    private fun showCreateReminderDialog() {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.reminder_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val id_project = Global.idProject
        val titleReminderEditText = dialogLayout.findViewById<EditText>(R.id.titleReminder)
        val dateEditText = dialogLayout.findViewById<EditText>(R.id.dateReminderEditText)
        val workTextInputLayout =
            dialogLayout.findViewById<TextInputLayout>(R.id.reminderTextInputLayout)

        val works = getWorks(Global.idProject)

        autoCompleteWork = dialogLayout.findViewById(R.id.list_reminder)

        adapterItemsWork = ArrayAdapter<String>(requireContext(), R.layout.list_item_role, works)

        autoCompleteWork.setAdapter(adapterItemsWork)

        dateEditText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                requireContext(),
                R.style.DatePickerStyle,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = Calendar.getInstance()
                    selectedDate.set(year, monthOfYear, dayOfMonth)
                    val formattedDate = SimpleDateFormat(
                        "dd-MM-yyyy",
                        Locale.getDefault()
                    ).format(selectedDate.time)
                    dateEditText.setText(formattedDate)
                },
                year,
                month,
                day
            )

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

        val nextButtom = dialog.findViewById<ImageButton>(R.id.nextReminderButton)
        nextButtom.setOnClickListener {
            val title = titleReminderEditText.text.toString()
            val date = dateEditText.text.toString()
            val workTextInputEditText = workTextInputLayout.editText
            val work = workTextInputEditText?.text?.toString()

            val success = addReminder(id_project, title, date, work!!)

            if (success) {
                getReminders(Global.idProject)
                recycler.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun addReminder(id_project: Int, title: String, date: String, work: String): Boolean {
        var success = false

        if (id_project != 0) {
            if (!title.isEmpty()){
                if(!date.isEmpty()){
                    if(!work.isEmpty()){
                        val reminder = Reminder(0,title,Global.idUser,Global.idProject,work, date)
                        val status = sqLiteHelper.InsertReminder(reminder)

                        if(status > -1) {
                            success = true
                            Toast.makeText(context, "Agregado exitosamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(context, "Acción fallida", Toast.LENGTH_SHORT).show()
                        }

                    }else{
                        Toast.makeText(context, "No hay una tarea seleccionada", Toast.LENGTH_SHORT).show()
                    }
                }else{
                    Toast.makeText(context, "Campo fecha vacío", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Campo título vacío", Toast.LENGTH_SHORT).show()
            }
        }
        return success
    }

    private fun getReminders(id: Int){
        val reminderList = sqLiteHelper.GetAllReminders()
        val reminders = reminderList.filter { it.id_project == id }

        listReminders.clear()

        for (reminder in reminders){
            listReminders.add(Reminder(reminder.id, reminder.title,reminder.id_user,reminder.id_project,reminder.work,reminder.date))
        }

        //Title page
        val projectList = sqLiteHelper.GetAllProjects()
        val projects = projectList.filter { it.id == id }

        listProjects.clear()

        for (project in projects){
            listProjects.add(Project(project.id,project.id_user,project.title,project.date_start,project.date_end,project.description))
        }

        val titlePageTextView = binding.TitlePageReminder
        val titlePage = listProjects[0]
        val title = "Notificaciones de '"+titlePage.title+"'"
        titlePageTextView.setText(title)

    }

    private fun getWorks(id: Int): ArrayList<String>{
        val workList = sqLiteHelper.GetAllWorks()
        val works = workList.filter { it.id_project == id }
        var listWorks: ArrayList<String> = ArrayList()

        listWorks.clear()

        for (work in works){
            listWorks.add(work.title)
        }
        return listWorks
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