package com.antonsmart.protrack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.R
import com.antonsmart.protrack.objects.Note

class NoteAdapter(var context: Context, var listNotes:MutableList<Note>):RecyclerView.Adapter<NoteAdapter.MyHolder>(){

    inner class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        lateinit var title: TextView
        lateinit var item: View

        init {
            title = itemView.findViewById(R.id.titleProject)
            item = itemView.findViewById(R.id.itemProject)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
     var itemView = LayoutInflater.from(context).inflate(R.layout.item_project_layout,parent,false)

     return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listNotes.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var note = listNotes[position]
        holder.title.text = note.title

        val backgrounds = arrayOf(R.drawable.item_recycler_view1,R.drawable.item_recycler_view2)
        val backgound = backgrounds[position % backgrounds.size]
        holder.item.setBackgroundResource(backgound)
    }
}