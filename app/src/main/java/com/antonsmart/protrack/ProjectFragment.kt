package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.ProjectAdapter
import com.antonsmart.protrack.dataClass.Project

class ProjectFragment : Fragment(R.layout.fragment_project) {

    private var listProjects: MutableList<Project> = mutableListOf()
    private lateinit var recycler: RecyclerView
    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listProjects.add(Project("Proyecto 1"))
        listProjects.add(Project("Proyecto 2"))
        listProjects.add(Project("Proyecto 3"))

        setAdapter()
    }

    private fun setAdapter() {
        recycler = requireView().findViewById(R.id.projectRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = ProjectAdapter(requireContext(), listProjects)
    }

}