package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.NoteListWorksWithoutButtonsAdapter
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageListWorksNoteBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Project
import com.antonsmart.protrack.objects.Work

class PageListWorksNoteFragment : Fragment(R.layout.fragment_page_list_works_note) {
    private lateinit var binding: FragmentPageListWorksNoteBinding
    private var listWorks: MutableList<Work> = mutableListOf()
    private var listProjects: MutableList<Project> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private lateinit var sqLiteHelper: SQLiteHelper


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqLiteHelper = SQLiteHelper(requireContext())

        getWorks(Global.idProject)
        getProjects(Global.idProject)


        setAdapter()

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapter() {
        recycler = requireView().findViewById(R.id.page_list_works_noteRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = NoteListWorksWithoutButtonsAdapter(requireContext(), listWorks)
    }

    private fun getWorks(id_project: Int) {
        val workList = sqLiteHelper.GetAllWorks()
        val works = workList.filter { it.id_project == id_project }

        listWorks.clear()

        for (work in works){
            listWorks.add(Work(work.id, work.id_project, work.id_user, work.title, work.description, work.date_start, work.date_end, work.person, work.role, work.finish))
        }


    }

    private fun getProjects(id: Int) {
        val projectList = sqLiteHelper.GetAllProjects()
        val projects = projectList.filter { it.id == id }

        listProjects.clear()

        for (project in projects) {
            listProjects.add(Project(project.id, project.id_user, project.title, project.date_start, project.date_end, project.description))
        }


            val texViewTitle = binding.titleListWorksPage
            val titlePage = listProjects[0]
            val title = titlePage.title
            texViewTitle.setText(title)
    }
}
