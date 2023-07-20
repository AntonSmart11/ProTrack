package com.antonsmart.protrack

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageProjectBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Project
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PageProjectFragment : Fragment(R.layout.fragment_page_project) {

    private lateinit var binding: FragmentPageProjectBinding
    private val args: PageProjectFragmentArgs by navArgs()
    private lateinit var sqliteHelper: SQLiteHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageProjectBinding.bind(view)

        sqliteHelper = SQLiteHelper(requireContext())

        val projectId = args.idProject
        Global.idProject = projectId
        val project = getProject(projectId)

        //Content
        val title = binding.titleProjectPage
        val description = binding.descriptionProjectPage
        val dateStart = binding.dateStartProjectPage
        val dateEnd = binding.dateEndProjectPage

        updatePage(project, title, description, dateStart, dateEnd)

        //Buttons
        val btnEdit = binding.editProject
        val btnDelete = binding.deleteProject

        btnEdit.setOnClickListener {
            showEditProjectDialog(projectId, project, title, description, dateStart, dateEnd)
        }

        btnDelete.setOnClickListener {
            DeleteProject(projectId, view)
        }

        val numWorks = getWorks(projectId)

        if(numWorks > 0) {
            binding.noWorks.visibility = View.GONE
            binding.worksBar.visibility = View.VISIBLE

            //Progress Bar
            val progressBar = binding.progressBar
            val workFinish = getWorksFinish(projectId)

            val numProgress = workFinish.toDouble()/numWorks
            val numLeft = 1 - numProgress
            val porcent = (numProgress * 100).toInt()

            progressBar.post {

                val progressBarWidth = progressBar.width

                val progress = (progressBarWidth * numProgress).toFloat()
                val background = (progressBarWidth * numLeft).toFloat()

                val progressLayout = binding.progressBarColor1
                val backgroundLayout = binding.progressBarColor2

                val layoutParams1 = progressLayout.layoutParams as LinearLayout.LayoutParams
                val layoutParams2 = backgroundLayout.layoutParams as LinearLayout.LayoutParams

                layoutParams1.weight = progress
                layoutParams2.weight = background

                progressLayout.layoutParams = layoutParams1
                backgroundLayout.layoutParams = layoutParams2

            }

            binding.workPorcentComplete.setText("$porcent%")
            binding.workCompleteText.setText("Se han completado $workFinish tareas de un total de $numWorks tareas.")
        } else {
            binding.worksBar.visibility = View.GONE
            binding.noWorks.visibility = View.VISIBLE
        }

        binding.toWork.setOnClickListener {
            findNavController().navigate(R.id.action_pageProjectFragment_to_workFragment)
        }

        binding.arrowBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }

    }

    private fun DeleteProject(id: Int, view: View) {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.delete_project_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val nextButton = dialog.findViewById<Button>(R.id.btnNextDeleteProject)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelDeleteProject)

        nextButton.setOnClickListener {
            sqliteHelper.DeleteProject(id)
            dialog.dismiss()
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun  showEditProjectDialog(id: Int, project1: Project, title1: TextView, description1: TextView, date_start1:TextView, date_end1: TextView) {
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

            datePickerDialog.show()
        }

        val project = getProject(id)

        titleEditText.setText(project.title)
        dateStartEditText.setText(project.date_start)
        dateEndEditText.setText(project.date_end)
        descriptionEditText.setText(project.description)

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextProjectButton)
        nextButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val date_start = dateStartEditText.text.toString()
            val date_end = dateEndEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if(!title.isEmpty()) {
                if(!date_end.isEmpty()) {
                    if(!description.isEmpty()) {
                        val project = Project(id, Global.idUser, title, date_start, date_end, description)
                        val status = sqliteHelper.UpdateProject(project)

                        if(status > -1) {
                            Toast.makeText(requireContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show()
                            dialog.dismiss()

                            updatePage(project, title1, description1, date_start1, date_end1)
                        } else {
                            Toast.makeText(requireContext(), "Acción fallida", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Campo descripción vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Campo fecha vacío", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Campo título vacío", Toast.LENGTH_SHORT).show()
            }

        }

        dialog.show()
    }

    private fun updatePage(project: Project, title: TextView, description: TextView, date_start: TextView, date_end: TextView) {
        title.setText(project.title)
        description.setText(project.description)
        date_start.setText(project.date_start)
        date_end.setText(project.date_end)
    }

    private fun getProject(id: Int): Project {
        val projectList = sqliteHelper.GetAllProjects()
        val project = projectList.find { it.id == id }

        return project!!
    }

    private fun getWorks(id: Int): Int {
        val workList = sqliteHelper.GetAllWorks()
        val works = workList.filter { it.id_project == id }

        return works.size
    }

    private fun getWorksFinish(id: Int): Int {
        val workList = sqliteHelper.GetAllWorks()
        val works = workList.filter { it.id_project == id }
        val worksFinish = works.filter { it.finish == "TRUE" }

        return worksFinish.size
    }

}