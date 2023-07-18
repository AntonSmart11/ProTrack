package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.ReminderListProjectWithoutButtonsAdapter
import com.antonsmart.protrack.databinding.FragmentNoteBinding
import com.antonsmart.protrack.databinding.FragmentRemindersBinding
import com.antonsmart.protrack.objects.Project


class RemindersFragment : Fragment(R.layout.fragment_reminders) {
    private lateinit var binding: FragmentRemindersBinding
    private var listProjects: MutableList<Project> = mutableListOf()
    private lateinit var recycler: RecyclerView


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        listProjects.clear()

        setAdapter()

        binding.arrowBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapter(){
        recycler = requireView().findViewById(R.id.remindersRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ReminderListProjectWithoutButtonsAdapter(requireContext(), listProjects)
    }

}