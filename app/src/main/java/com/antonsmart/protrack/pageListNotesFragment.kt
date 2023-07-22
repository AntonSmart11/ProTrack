package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.Dialog
import android.icu.text.CaseMap.Title
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.NoteListNotesWithoutButtonsAdapter
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageListNotesBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Note

class pageListNotesFragment : Fragment(R.layout.fragment_page_list_notes) {
    private lateinit var binding: FragmentPageListNotesBinding
    private var listNotes: MutableList<Note> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private lateinit var sqLiteHelper: SQLiteHelper


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqLiteHelper = SQLiteHelper(requireContext())

        val id_work = Global.idWork
        getNotes(id_work)

        setAdapter()

        binding.addNote.setOnClickListener {
            showCreateNoteDialog()
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapter() {
        recycler = requireView().findViewById(R.id.listNotesRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = NoteListNotesWithoutButtonsAdapter(requireContext(), listNotes)
    }

    private fun showCreateNoteDialog() {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.note_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val id_work = Global.idWork
        val noteNameEditText = dialogLayout.findViewById<EditText>(R.id.titleNote)
        val noteDescritionEditText = dialogLayout.findViewById<EditText>(R.id.descriptionNote)

        val nextButtom = dialog.findViewById<ImageButton>(R.id.nextNoteButton)
        nextButtom.setOnClickListener {

            val noteName = noteNameEditText.text.toString()
            val noteDescription = noteDescritionEditText.text.toString()

            val success = addNote(id_work, noteName, noteDescription)

            if (success) {
                getNotes(Global.idWork)
                recycler.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }


        }

        dialog.show()
    }

    private fun addNote(id_work: Int, title: String, description: String): Boolean {
        var success = false

        if (id_work != 0) {
            if (!title.isEmpty()) {
                if (!description.isEmpty()) {

                    val note = Note(0, id_work, title, description)
                    val status = sqLiteHelper.InsertNote(note)

                    if (status > -1) {
                        success = true
                        Toast.makeText(context, "Agregado exitosamente", Toast.LENGTH_SHORT).show()
                    }
                }else {
                    Toast.makeText(context, "Campo descripción vacío", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(context, "Campo nombre vacío", Toast.LENGTH_SHORT).show()
            }
        }
        return success
    }

    private  fun getNotes(id: Int){
        val noteList = sqLiteHelper.GetAllNotes()
        val notes = noteList.filter { it.id_work == id }

        listNotes.clear()

        for (note in notes){
            listNotes.add(Note(note.id,note.id_work,note.title,note.description))
        }
    }
}