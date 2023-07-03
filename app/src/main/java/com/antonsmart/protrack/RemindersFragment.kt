package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.ReminderAdapter
import com.antonsmart.protrack.databinding.FragmentRemindersBinding
import com.antonsmart.protrack.objects.Reminder
import java.text.SimpleDateFormat


class RemindersFragment : Fragment(R.layout.fragment_reminders){

    private lateinit var binding: FragmentRemindersBinding
    private var listReminders: MutableList<Reminder> = mutableListOf()
    private lateinit var recycler: RecyclerView

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        /*ejemplo de como insertar una fecha en el objeto correctamente
*
* */
        val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
        val fechaString = "2/7/2023"
        val fecha = formatoFecha.parse(fechaString)
        //////////////////////////////////////////////////////////

        listReminders.add(Reminder("Recordatorio 1", "Descripción 1", fecha))
        listReminders.add(Reminder("Recordatorio 2", "Descripción 2", fecha))
        listReminders.add(Reminder("Recordatorio 3", "Descripción 3", fecha))
        listReminders.add(Reminder("Recordatorio 4", "Descripción 4", fecha))
        listReminders.add(Reminder("Recordatorio 5", "Descripción 5", fecha))

        setAdapter()

        binding.addReminders.setOnClickListener {
            showCreateReminderDialog()
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapter(){
        recycler = requireView().findViewById(R.id.remindersRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter =ReminderAdapter(requireContext(),listReminders)
    }

    private fun showCreateReminderDialog(){
        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.reminder_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val nextButtom = dialog.findViewById<ImageButton>(R.id.nextReminderButton)
        nextButtom.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}