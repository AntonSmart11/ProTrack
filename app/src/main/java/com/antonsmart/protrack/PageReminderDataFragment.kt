package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.antonsmart.protrack.databinding.FragmentPageReminderDataBinding


class PageReminderDataFragment : Fragment(R.layout.fragment_page_reminder_data) {
    private lateinit var binding: FragmentPageReminderDataBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageReminderDataBinding.bind(view)

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }
}