package com.antonsmart.protrack

import android.content.Context
import android.os.Bundle
import android.provider.Settings.Global
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentLoginBinding
import com.antonsmart.protrack.objects.User

class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sqliteHelper: SQLiteHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqliteHelper = SQLiteHelper(requireContext())

        val sharedPrefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val sessionActive = sharedPrefs.getBoolean("session_active", false)
        val userId = sharedPrefs.getInt("id_user", 0)

        if(sessionActive) {
            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        }

        binding.btnEnter.setOnClickListener {
            val username = binding.loginUser.text.toString()
            val password = binding.loginPassword.text.toString()

            if(username.isEmpty()) {
                Toast.makeText(context, "Campo usuario vacío", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(context, "Campo contraseña vacío", Toast.LENGTH_SHORT).show()
            } else {

                if(ValidationUsername(username)) {
                    if(ValidationPassword(username, password))  {
                        Toast.makeText(context, "Iniciado exitosamente", Toast.LENGTH_SHORT).show()

                        val id_user = UserId(username).id

                        SessionActive(id_user)

                        val direction = LoginFragmentDirections.actionLoginFragmentToDashboardFragment(id_user)
                        findNavController().navigate(direction)

                    } else {
                        Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Usuario inexistente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }
    }

    private fun SessionActive(id: Int) {
        val sharedPrefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("session_active", true)
        editor.putInt("id_user", id)
        editor.apply()
    }

    private fun UserId(username: String): User {
        val userList = sqliteHelper.GetAllUsers()
        val user = userList.find { it.username == username }

        return user!!
    }

    private fun ValidationPassword(username: String, password: String): Boolean {
        val userList = sqliteHelper.GetAllUsers()
        val user = userList.find { it.username == username }
        var right = false

        if(user!!.password == password) {
            right = true
        }

        return right
    }

    private fun ValidationUsername(username: String): Boolean {
        val userList = sqliteHelper.GetAllUsers()
        val usernames = userList.map { it.username }
        var exists = false

        for (u in usernames) {
            if(username.contains(u)) {
                exists = true
            }
        }

        return exists
    }

}