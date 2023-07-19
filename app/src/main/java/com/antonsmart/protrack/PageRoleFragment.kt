package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.antonsmart.protrack.databinding.FragmentPageRoleBinding

class PageRoleFragment : Fragment(R.layout.fragment_page_role) {

    private lateinit var binding: FragmentPageRoleBinding


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageRoleBinding.bind(view)

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}

