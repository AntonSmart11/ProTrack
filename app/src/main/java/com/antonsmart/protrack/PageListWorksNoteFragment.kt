package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.NoteListWorksWithoutButtonsAdapter
import com.antonsmart.protrack.databinding.FragmentPageListWorksNoteBinding
import com.antonsmart.protrack.objects.Work

class PageListWorksNoteFragment : Fragment(R.layout.fragment_page_list_works_note) {
    private lateinit var binding: FragmentPageListWorksNoteBinding
    private var listWorks: MutableList<Work> = mutableListOf()
    private lateinit var recycler: RecyclerView


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        listWorks.clear()
        listWorks.add(Work("Tarea 1"))
        listWorks.add(Work("Tarea 2"))
        listWorks.add(Work("Tarea 3"))
        listWorks.add(Work("Tarea 4"))
        listWorks.add(Work("Tarea 5"))

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
}
