package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.antonsmart.protrack.databinding.FragmentUserBinding

class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var binding: FragmentUserBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)!!

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

}