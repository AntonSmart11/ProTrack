package com.antonsmart.protrack

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageNoteBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Note
import com.antonsmart.protrack.objects.Role


class PageNoteFragment : Fragment(R.layout.fragment_page_note) {

    private lateinit var binding: FragmentPageNoteBinding
    private val args: PageNoteFragmentArgs by navArgs()
    private lateinit var sqLiteHelper: SQLiteHelper



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageNoteBinding.bind(view)
        sqLiteHelper = SQLiteHelper(requireContext())


        val noteIdARGS = Global.idNote
        val noteId= args.idNote

        Log.d("id nota global",Global.idNote.toString())
        Log.d("id nota argumento",args.idNote.toString())

        val note = getNote(noteId)

        //Content
        val title = binding.titlePageNote
        val description = binding.descriptionNotePage

        updatePage(note,title,description)

        //Buttons
        val btnEdit = binding.editNote
        val btnDelete = binding.deleteNote

        btnEdit.setOnClickListener {
            showEditNoteDialog(noteId,note,title,description)
        }

        btnDelete.setOnClickListener {
            DeleteNote(noteId)
        }


        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun DeleteNote(id: Int){
        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.delete_note_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val nextButton = dialog.findViewById<Button>(R.id.btnNextDeleteNote)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelDeleteNote)

        nextButton.setOnClickListener {
            sqLiteHelper.DeleteNote(id)
            dialog.dismiss()
            findNavController().navigateUp()
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditNoteDialog(id: Int, note1: Note, title1: TextView, description1: TextView){
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.note_alert_dialog,null)
        dialog.setContentView(dialogLayout)

        val titleEditText = dialogLayout.findViewById<EditText>(R.id.titleNote)
        val descriptionEditText = dialog.findViewById<EditText>(R.id.descriptionNote)

        val note = getNote(id)

        titleEditText.setText(note.title)
        descriptionEditText.setText(note.description)

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextNoteButton)
        nextButton.setOnClickListener {
            val titlePage = titleEditText.text.toString()
            val noteDescription = descriptionEditText.text.toString()

            if (!titlePage.isEmpty()){
                if (!noteDescription.isEmpty()){
                    val note = Note(id,Global.idWork,titlePage,noteDescription)
                    val status = sqLiteHelper.UpdateNote(note)

                    if(status  > -1) {
                        Toast.makeText(requireContext(),"Actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()

                        updatePage(note,title1,description1)
                    }

                }else {
                    Toast.makeText(requireContext(), "Campo descripción vacío", Toast.LENGTH_SHORT)
                        .show()
                }
            }else{
            Toast.makeText(requireContext(), "Campo nombre vacío", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

    private fun updatePage(note: Note, title: TextView, description: TextView){
        title.setText(note.title)
        description.setText(note.description)
    }

    private fun getNote(id: Int): Note {
        val noteList = sqLiteHelper.GetAllNotes()
        val note = noteList.find { it.id == id }

        return note!!
    }


}