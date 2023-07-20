package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.NoteListProjectsWithoutButtonsAdapter
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentNoteBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Project


//se usan los adaptadores de NoteListProjectsWithoutButton adapter para mostrar
// una lista de los porjectos para acceder a sus respectivas notas
//ademas de que aparecen los projectos sin los botones de eliminar y editar

class NoteFragment : Fragment(R.layout.fragment_note) {
    private lateinit var binding: FragmentNoteBinding
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

        binding.arrowBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapter(){
        recycler = requireView().findViewById(R.id.noteRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = NoteListProjectsWithoutButtonsAdapter(requireContext(), listProjects)
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