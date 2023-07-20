package com.antonsmart.protrack

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.WorkAdapter
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentWorkBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Work
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class WorkFragment : Fragment(R.layout.fragment_work) {

    private lateinit var binding: FragmentWorkBinding
    private var listWorks: MutableList<Work> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private lateinit var sqliteHelper: SQLiteHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqliteHelper = SQLiteHelper(requireContext())

        getWorks(Global.idProject)

        setAdapter()

        binding.addWork.setOnClickListener {
            showCreateWorkDialog()
        }

        binding.arrowBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }

    }

    private fun setAdapter() {
        recycler = requireView().findViewById(R.id.workRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = WorkAdapter(requireContext(), listWorks)
    }

    private fun  showCreateWorkDialog() {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.work_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val id_project = Global.idProject
        val titleEditText = dialogLayout.findViewById<EditText>(R.id.titleWorkEditText)
        val personEditText = dialogLayout.findViewById<EditText>(R.id.personWorkEditText)
        val roleEditText = dialogLayout.findViewById<EditText>(R.id.roleWorkEditText)
        val dateStartEditText = dialogLayout.findViewById<EditText>(R.id.dateStartWorkEditText)
        val dateEndEditText = dialogLayout.findViewById<EditText>(R.id.dateEndWorkEditText)
        val descriptionEditText = dialogLayout.findViewById<EditText>(R.id.descriptionWorkEditText)

        dateStartEditText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), R.style.DatePickerStyle, {_, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
                dateStartEditText.setText(formattedDate)
            }, year, month, day)

            //Set min date
            val minDateParts = minDate(id_project)
            val minParts = minDateParts.split("-")
            val dayMinPart = minParts[0]
            val monthMinPart = minParts[1]
            val yearMinPart = minParts[2]

            val calendarMin = Calendar.getInstance()
            calendarMin.set(yearMinPart.toInt(), monthMinPart.toInt(), dayMinPart.toInt())
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
            calendarMax.set(yearMaxPart.toInt(), monthMaxPart.toInt(), dayMaxPart.toInt())
            val maxDate = calendarMax.timeInMillis
            datePickerDialog.datePicker.maxDate = maxDate

            datePickerDialog.show()
        }

        dateEndEditText.setOnClickListener {
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(requireContext(), R.style.DatePickerStyle, {_, year, monthOfYear, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, monthOfYear, dayOfMonth)
                val formattedDate = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(selectedDate.time)
                dateEndEditText.setText(formattedDate)
            }, year, month, day)

            //Set min date
            val minDateParts = minDate(id_project)
            val minParts = minDateParts.split("-")
            val dayMinPart = minParts[0]
            val monthMinPart = minParts[1]
            val yearMinPart = minParts[2]

            val calendarMin = Calendar.getInstance()
            calendarMin.set(yearMinPart.toInt(), monthMinPart.toInt(), dayMinPart.toInt())
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
            calendarMax.set(yearMaxPart.toInt(), monthMaxPart.toInt(), dayMaxPart.toInt())
            val maxDate = calendarMax.timeInMillis
            datePickerDialog.datePicker.maxDate = maxDate
        }

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextWorkButton)
        nextButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val person = personEditText.text.toString()
            val role = roleEditText.text.toString()
            val date_start = dateStartEditText.text.toString()
            val date_end = dateEndEditText.text.toString()
            val description = descriptionEditText.text.toString()

            val success = addWork(id_project, title, person, role, date_start, date_end, description)

            if(success) {
                getWorks(Global.idProject)
                recycler.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }
        }

        dialog.show()
    }

    private fun addWork(id_project: Int, title: String, person: String, role: String, date_start: String, date_end: String, description: String): Boolean {
        var success = false

        if(id_project != 0) {
            if(!title.isEmpty()) {
                if(!person.isEmpty()) {
                    if(!role.isEmpty()) {
                        if(!date_end.isEmpty()) {
                            if(!description.isEmpty()) {
                                val work = Work(0, id_project, Global.idUser, title, description, date_start, date_end, person, role, "FALSE")
                                val status = sqliteHelper.InsertWork(work)

                                if(status > -1) {
                                    success = true
                                    Toast.makeText(context, "Agregado exitosamente", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(context, "Acción fallida", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(context, "Campo descripción vacío", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Campo fecha vacío", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Campo rol vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Campo encargado vacío", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(context, "Campo título vacío", Toast.LENGTH_SHORT).show()
            }
        }

        return success
    }

    private fun getWorks(id: Int) {
        val workList = sqliteHelper.GetAllWorks()
        val works = workList.filter { it.id_project == id }

        listWorks.clear()

        for (work in works){
            listWorks.add(Work(work.id, work.id_project, work.id_user, work.title, work.description, work.date_start, work.date_end, work.person, work.role, work.finish))
        }
    }

    private fun minDate(id: Int): String {
        val projectList = sqliteHelper.GetAllProjects()
        val project = projectList.find { it.id == id }

        val date_start = project!!.date_start

        return date_start
    }

    private fun maxDate(id: Int): String {
        val projectList = sqliteHelper.GetAllProjects()
        val project = projectList.find { it.id == id }

        val date_end = project!!.date_end

        return date_end
    }

}