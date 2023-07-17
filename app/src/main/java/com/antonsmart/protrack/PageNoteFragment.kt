package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.antonsmart.protrack.databinding.FragmentPageNoteBinding


class PageNoteFragment : Fragment(R.layout.fragment_page_note) {

    private lateinit var binding: FragmentPageNoteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageNoteBinding.bind(view)

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }


}