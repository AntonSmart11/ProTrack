package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageWorkBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Work
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class PageWorkFragment : Fragment(R.layout.fragment_page_work) {
    private lateinit var binding: FragmentPageWorkBinding
    private val args: PageWorkFragmentArgs by navArgs()
    private lateinit var sqliteHelper: SQLiteHelper

    @SuppressLint("ResourceAsColor")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageWorkBinding.bind(view)

        sqliteHelper = SQLiteHelper(requireContext())
        
        val workId = args.idWork
        
        val work = getWork(workId)
        
        //Content
        val title = binding.titleWorkPage
        val description = binding.descriptionWorkPage
        val date_start = binding.dateStartWorkPage
        val date_end = binding.dateEndWorkPage
        val person = binding.personWorkPage
        val role = binding.rolWorkPage
        
        updatePage(work, title, description, date_start, date_end, person, role)

        //Buttons
        val btnEdit = binding.editWork
        val btnDelete = binding.deleteWork
        val btnFinish = binding.finishWork

        btnEdit.setOnClickListener {
            showEditWorkDialog(workId, work, title, description, date_start, date_end, person, role)
        }

        btnDelete.setOnClickListener {
            DeleteWork(workId, view)
        }

        btnFinish.setOnClickListener {
            FinishWork(workId)

            val finish = FinishStatus(workId)

            if(finish == "TRUE") {
                btnFinish.setText("No terminado")
                btnFinish.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.notSuccess))
                binding.linearLayoutFinish.visibility = View.VISIBLE
            } else {
                btnFinish.setText("Terminado")
                btnFinish.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.success))
                binding.linearLayoutFinish.visibility = View.GONE
            }
        }

        binding.arrowBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }
    }

    private fun FinishStatus(id: Int): String {
        val workList = sqliteHelper.GetAllWorks()
        val work = workList.find { it.id == id }

        val status = work!!.finish

        return status
    }
    private fun FinishWork(id: Int) {
        val finishStatus = FinishStatus(id)
        var status = -1

        if(finishStatus == "FALSE") {
            status = sqliteHelper.UpdateWorkFinish(id, "TRUE")
            Toast.makeText(requireContext(), "Tarea terminada", Toast.LENGTH_SHORT).show()
        } else {
            status = sqliteHelper.UpdateWorkFinish(id, "FALSE")
            Toast.makeText(requireContext(), "Tarea no terminada", Toast.LENGTH_SHORT).show()
        }
    }

    private fun DeleteWork(id: Int, view: View) {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.delete_work_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val nextButton = dialog.findViewById<Button>(R.id.btnNextDeleteWork)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelDeleteWork)

        nextButton.setOnClickListener {
            sqliteHelper.DeleteWork(id)
            dialog.dismiss()
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditWorkDialog(
        id: Int,
        work1: Work,
        title1: TextView,
        description1: TextView,
        dateStart1: TextView,
        dateEnd1: TextView,
        person1: TextView,
        role1: TextView
    ) {
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

        val work = getWork(id)

        titleEditText.setText(work.title)
        personEditText.setText(work.person)
        roleEditText.setText(work.role)
        dateStartEditText.setText(work.date_start)
        dateEndEditText.setText(work.date_end)
        descriptionEditText.setText(work.description)

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextWorkButton)
        nextButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val person = personEditText.text.toString()
            val role = roleEditText.text.toString()
            val date_start = dateStartEditText.text.toString()
            val date_end = dateEndEditText.text.toString()
            val description = descriptionEditText.text.toString()

            if(!title.isEmpty()) {
                if(!person.isEmpty()) {
                    if (!role.isEmpty()) {
                        if (!date_start.isEmpty()) {
                            if(!date_end.isEmpty()) {
                                if(!description.isEmpty()) {
                                    val work = Work(id, id_project, Global.idUser, title, description, date_start, date_end, person, role, work1.finish)
                                    val status = sqliteHelper.UpdateWork(work)

                                    if (status > -1) {
                                        Toast.makeText(requireContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show()
                                        dialog.dismiss()

                                        updatePage(work, title1, description1, dateStart1, dateEnd1, person1, role1)
                                    } else {
                                        Toast.makeText(requireContext(), "Acción fallida", Toast.LENGTH_SHORT).show()
                                    }
                                } else {
                                    Toast.makeText(requireContext(), "Campo descripción vacío", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(requireContext(), "Campo fecha estimada vacío", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(requireContext(), "Campo fecha inicio vacío", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(requireContext(), "Campo rol vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Campo encargado vacío", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Campo título vacío", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun updatePage(work: Work, title: TextView, description: TextView, dateStart: TextView, dateEnd: TextView, person: TextView, role: TextView) {
        title.setText(work.title)
        description.setText(work.description)
        dateStart.setText(work.date_start)
        dateEnd.setText(work.date_end)
        person.setText(work.person)
        role.setText(work.role)
    }

    private fun getWork(id: Int): Work {
        val workList = sqliteHelper.GetAllWorks()
        val work = workList.find { it.id == id }
        
        return work!!
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