package com.antonsmart.protrack.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.R
import com.antonsmart.protrack.WorkFragment
import com.antonsmart.protrack.objects.Project

class ProjectAdapter(var context: Context, var listProjects:MutableList<Project>):RecyclerView.Adapter<ProjectAdapter.MyHolder>() {

    inner class MyHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        lateinit var title: TextView
        lateinit var item: View

        init {
            title = itemView.findViewById(R.id.titleProject)
            item = itemView.findViewById(R.id.itemProject)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        var itemView = LayoutInflater.from(context).inflate(R.layout.item_project_layout, parent, false)

        return MyHolder(itemView)
    }

    override fun getItemCount(): Int {
        return listProjects.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        var project = listProjects[position]
        holder.title.text = project.title

        val backgrounds = arrayOf(R.drawable.item_recycler_view1, R.drawable.item_recycler_view2)
        val background = backgrounds[position % backgrounds.size]
        holder.item.setBackgroundResource(background)

        holder.item.setOnClickListener {
            val navController = Navigation.findNavController(holder.itemView)
            navController.navigate(R.id.action_projectFragment_to_pageProjectFragment)
        }
    }

}