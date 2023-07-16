package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentUserBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.User

class UserFragment : Fragment(R.layout.fragment_user) {

    private lateinit var binding: FragmentUserBinding
    private lateinit var sqliteHelper: SQLiteHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)!!
        sqliteHelper = SQLiteHelper(requireContext())

        //Global variable
        val idUser = Global.idUser

        //Get user
        val user = user(idUser)

        //Content
        binding.userName.setText(user.name)
        binding.userLast.setText(user.last)
        binding.userUsername.setText(user.username)
        binding.userPassword.setText(user.password)

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun user(id: Int): User {
        val userList = sqliteHelper.GetAllUsers()
        val user = userList.find { it.id == id }

        return user!!
    }

}