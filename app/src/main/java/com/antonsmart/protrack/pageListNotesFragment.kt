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
import com.antonsmart.protrack.adapters.NoteListNotesWithoutButtonsAdapter
import com.antonsmart.protrack.databinding.FragmentPageListNotesBinding
import com.antonsmart.protrack.objects.Note

class pageListNotesFragment : Fragment(R.layout.fragment_page_list_notes){
    private lateinit var binding: FragmentPageListNotesBinding
    private var listNotes: MutableList<Note> = mutableListOf()
    private lateinit var recycler : RecyclerView


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        listNotes.clear()
        listNotes.add(Note("Nota 1", "Descripción 1"))
        listNotes.add(Note("Nota 2", "Descripción 2"))
        listNotes.add(Note("Nota 3", "Descripción 3"))
        listNotes.add(Note("Nota 4", "Descripción 4"))
        listNotes.add(Note("Nota 5", "Descripción 5"))

        setAdapter()

        binding.addNote.setOnClickListener{
            showCreateNoteDialog()
        }

        binding.arrowBack.setOnClickListener{
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapter(){
        recycler = requireView().findViewById(R.id.listNotesRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = NoteListNotesWithoutButtonsAdapter(requireContext(),listNotes)
    }

    private fun showCreateNoteDialog(){
        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.note_alert_dialog,null)
        dialog.setContentView(dialogLayout)

        val nextButtom = dialog.findViewById<ImageButton>(R.id.nextNoteButton)
        nextButtom.setOnClickListener{
            dialog.dismiss()
        }

        dialog.show()
    }

}