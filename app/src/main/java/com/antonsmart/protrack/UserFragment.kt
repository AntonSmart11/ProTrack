package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentUserBinding

class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var binding: FragmentUserBinding
    private lateinit var sqliteHelper: SQLiteHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)!!


        sqliteHelper = SQLiteHelper(requireContext())

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun user(id: Int) {
        val userList = sqliteHelper.GetAllUsers()
        val user = userList.find { it.id == id }


    }

}