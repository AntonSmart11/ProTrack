package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.ProjectAdapter
import com.antonsmart.protrack.dataClass.Project
import com.antonsmart.protrack.databinding.FragmentProjectBinding

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private lateinit var binding: FragmentProjectBinding
    private var listProjects: MutableList<Project> = mutableListOf()
    private lateinit var recycler: RecyclerView

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        listProjects.add(Project("Proyecto 1"))
        listProjects.add(Project("Proyecto 2"))
        listProjects.add(Project("Proyecto 3"))
        listProjects.add(Project("Proyecto 4"))
        listProjects.add(Project("Proyecto 5"))

        setAdapter()

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapter() {
        recycler = requireView().findViewById(R.id.projectRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ProjectAdapter(requireContext(), listProjects)
    }
}