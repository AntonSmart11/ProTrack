package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.antonsmart.protrack.databinding.FragmentPageWorkBinding

class PageWorkFragment : Fragment(R.layout.fragment_page_work) {
    private lateinit var binding: FragmentPageWorkBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageWorkBinding.bind(view)

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}