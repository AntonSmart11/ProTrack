package com.antonsmart.protrack

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.WorkAdapter
import com.antonsmart.protrack.databinding.FragmentWorkBinding
import com.antonsmart.protrack.objects.Work

class WorkFragment : Fragment(R.layout.fragment_work) {

    private lateinit var binding: FragmentWorkBinding
    private var listWorks: MutableList<Work> = mutableListOf()
    private lateinit var recycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        listWorks.clear()
        listWorks.add(Work("Tarea 1"))
        listWorks.add(Work("Tarea 2"))
        listWorks.add(Work("Tarea 3"))
        listWorks.add(Work("Tarea 4"))
        listWorks.add(Work("Tarea 5"))
        listWorks.add(Work("Tarea 6"))

        setAdapter()

        binding.addWork.setOnClickListener {
            showCreateWorkDialog()
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
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

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextWorkButton)
        nextButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}