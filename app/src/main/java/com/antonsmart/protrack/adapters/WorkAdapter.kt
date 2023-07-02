package com.antonsmart.protrack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.R
import com.antonsmart.protrack.objects.Work

class WorkAdapter(var context: Context, var listWorks:MutableList<Work>):RecyclerView.Adapter<WorkAdapter.MyHolder>() {

    inner class MyHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        lateinit var title: TextView
        lateinit var item: View

        init {
            title = itemView.findViewById(R.id.titleWork)
            item = itemView.findViewById(R.id.itemWork)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_work_layout, parent, false)

        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listWorks.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var work = listWorks[position]
        holder.title.text = work.title

        val backgrounds = arrayOf(R.drawable.item_recycler_view1, R.drawable.item_recycler_view2)
        val background = backgrounds[position % backgrounds.size]
        holder.item.setBackgroundResource(background)

        holder.item.setOnClickListener {

        }
    }

}