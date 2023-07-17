package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
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

        getProjects(Global.idUser)

        setAdapter()

        binding.addProject.setOnClickListener {
            showCreateProjectDialog()
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
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
        val dateEditText = dialogLayout.findViewById<EditText>(R.id.dateProjectEditText)
        val descriptionEditText = dialogLayout.findViewById<EditText>(R.id.descriptionProjectEditText)

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

            datePickerDialog.show()
        }

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextProjectButton)
        nextButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val date = dateEditText.text.toString()
            val description = descriptionEditText.text.toString()

            val success = addProject(id_user, title, date, description)

            if(success) {
                getProjects(Global.idUser)
                recycler.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }

        }

        dialog.show()
    }

    private fun onDateSelected(day: Int, month: Int, year: Int, date: EditText) {
        date.setText("$day - $month - $year")
    }

    private fun addProject(id_user: Int, title: String, date: String, description: String): Boolean {
        var success = false

        if(id_user != 0) {
            if(!title.isEmpty()) {
                if(!date.isEmpty()) {
                    if(!description.isEmpty()) {
                        val project = Project(0, id_user, title, date, description)
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
            listProjects.add(Project(project.id, project.id_user, project.title, project.date, project.description))
        }
    }
}