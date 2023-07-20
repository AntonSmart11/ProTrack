package com.antonsmart.protrack

import android.annotation.SuppressLint
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
import com.antonsmart.protrack.adapters.ProjectAdapter
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.objects.Project
import com.antonsmart.protrack.databinding.FragmentProjectBinding
import com.antonsmart.protrack.global.Global
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private lateinit var binding: FragmentProjectBinding
    private var listProjects: MutableList<Project> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private lateinit var sqliteHelper: SQLiteHelper

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqliteHelper = SQLiteHelper(requireContext())

        Global.idProject = 0
        getProjects(Global.idUser)

        setAdapter()

        binding.addProject.setOnClickListener {
            showCreateProjectDialog()
        }

        binding.arrowBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }
    }

    private fun setAdapter() {
        recycler = requireView().findViewById(R.id.projectRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ProjectAdapter(requireContext(), listProjects)
    }

    private fun  showCreateProjectDialog() {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.project_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val id_user = Global.idUser
        val titleEditText = dialogLayout.findViewById<EditText>(R.id.titleProjectEditText)
        val dateStartEditText = dialogLayout.findViewById<EditText>(R.id.dateStartProjectEditText)
        val dateEndEditText = dialogLayout.findViewById<EditText>(R.id.dateEndProjectEditText)
        val descriptionEditText = dialogLayout.findViewById<EditText>(R.id.descriptionProjectEditText)

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
            val calendarMin = Calendar.getInstance()
            calendarMin.set(calendarMin.get(Calendar.YEAR), calendarMin.get(Calendar.MONTH), calendarMin.get(Calendar.DAY_OF_MONTH))
            val minDate = calendarMin.timeInMillis
            datePickerDialog.datePicker.minDate = minDate

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
            val calendarMin = Calendar.getInstance()
            calendarMin.set(calendarMin.get(Calendar.YEAR), calendarMin.get(Calendar.MONTH), calendarMin.get(Calendar.DAY_OF_MONTH))
            val minDate = calendarMin.timeInMillis
            datePickerDialog.datePicker.minDate = minDate

            datePickerDialog.show()
        }

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextProjectButton)
        nextButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val date_start = dateStartEditText.text.toString()
            val date_end = dateEndEditText.text.toString()
            val description = descriptionEditText.text.toString()

            val success = addProject(id_user, title, date_start, date_end, description)

            if(success) {
                getProjects(Global.idUser)
                recycler.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }

        }

        dialog.show()
    }

    private fun addProject(id_user: Int, title: String, date_start:String, date_end: String, description: String): Boolean {
        var success = false

        if(id_user != 0) {
            if(!title.isEmpty()) {
                if(!date_end.isEmpty()) {
                    if(!description.isEmpty()) {
                        val project = Project(0, id_user, title, date_start, date_end, description)
                        val status = sqliteHelper.InsertProject(project)

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
                Toast.makeText(context, "Campo título vacío", Toast.LENGTH_SHORT).show()
            }
        }

        return success
    }

    private fun getProjects(id: Int) {
        val projectList = sqliteHelper.GetAllProjects()
        val projects = projectList.filter { it.id_user == id }

        listProjects.clear()

        for (project in projects) {
            listProjects.add(Project(project.id, project.id_user, project.title, project.date_start, project.date_end, project.description))
        }
    }
}