package com.antonsmart.protrack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.R
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Note
import com.antonsmart.protrack.pageListNotesFragmentDirections

class NoteListNotesWithoutButtonsAdapter(
    private val context: Context,
    private val listNotes: MutableList<Note>
) : RecyclerView.Adapter<NoteListNotesWithoutButtonsAdapter.MyHolder>() {

    inner class MyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.titleItem_without_buttons)
        val item: View = itemView.findViewById(R.id.item_without_buttons)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_without_buttons_layout, parent, false)
        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val note = listNotes[position]
        holder.title.text = note.title

        val backgrounds = arrayOf(R.drawable.item_recycler_view1, R.drawable.item_recycler_view2)
        val background = backgrounds[position % backgrounds.size]
        holder.item.setBackgroundResource(background)

        holder.item.setOnClickListener{
            Global.idNote = note.id

            val navController = Navigation.findNavController(holder.itemView)
            val action = pageListNotesFragmentDirections.actionPageListNotesFragmentToPageNoteFragment(note.id)
            navController.navigate(action)
        }
    }
}
