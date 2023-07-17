package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.NoteAdapter
import com.antonsmart.protrack.adapters.NoteListProjectsWithoutButtonsAdapter
import com.antonsmart.protrack.databinding.FragmentNoteBinding
import com.antonsmart.protrack.objects.Note
import com.antonsmart.protrack.objects.Project


//se usan los adaptadores de NoteListProjectsWithoutButton adapter para mostrar
// una lista de los porjectos para acceder a sus respectivas notas
//ademas de que aparecen los projectos sin los botones de eliminar y editar

class NoteFragment : Fragment(R.layout.fragment_note) {
    private lateinit var binding: FragmentNoteBinding
    private var listProjects: MutableList<Project> = mutableListOf()
    private lateinit var recycler: RecyclerView


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!


        listProjects.clear()
        listProjects.add(Project("Projecto 1"))
        listProjects.add(Project("Projecto 2"))
        listProjects.add(Project("Projecto 3"))
        listProjects.add(Project("Projecto 4"))
        listProjects.add(Project("Projecto 5"))

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

}