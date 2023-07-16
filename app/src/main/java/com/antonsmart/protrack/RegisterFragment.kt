package com.antonsmart.protrack

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentRegisterBinding
import com.antonsmart.protrack.objects.User
import kotlin.math.log

class RegisterFragment : Fragment(R.layout.fragment_register) {

    private lateinit var binding: FragmentRegisterBinding
    private lateinit var sqliteHelper: SQLiteHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqliteHelper = SQLiteHelper(requireContext())

        binding.btnRegister.setOnClickListener {
            val name = binding.registerName.text.toString()
            val last = binding.registerLast.text.toString()
            val username = binding.registerUser.text.toString()
            val password = binding.registerPassword.text.toString()

            addUser(name, last, username, password)
        }

        binding.btnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment)
        }
    }

    private fun addUser(name: String, last: String, username: String, password: String) {

        var textFields = 0

        if(name.isEmpty()) {
            textFields += 1
        }

        if (last.isEmpty()) {
            textFields += 1
        }

        if (username.isEmpty()) {
            textFields += 1
        }

        if (password.isEmpty()) {
            textFields += 1
        }

        if (textFields > 1) {
            Toast.makeText(context, "Campos vacíos", Toast.LENGTH_SHORT).show()
        } else if(name.isEmpty()) {
            Toast.makeText(context, "Campo nombre vacío", Toast.LENGTH_SHORT).show()
        } else if (last.isEmpty()) {
            Toast.makeText(context, "Campo apellido vacío", Toast.LENGTH_SHORT).show()
        } else if (username.isEmpty()) {
            Toast.makeText(context, "Campo usuario vacío", Toast.LENGTH_SHORT).show()
        } else if (password.isEmpty()) {
            Toast.makeText(context, "Campo contraseña vacío", Toast.LENGTH_SHORT).show()
        } else {

            if(CheckUser(username)) {
                val user = User(0, name, last, username, password)
                val status = sqliteHelper.InsertUser(user)

                if(status > -1) {
                    Toast.makeText(context, "Registrado exitosamente", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.action_registerFragment2_to_loginFragment)
                } else {
                    Toast.makeText(context, "Acción fallida", Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.registerUser.text.clear()
                Toast.makeText(context, "Usuario repetido", Toast.LENGTH_SHORT).show()
            }

        }

    }

    private fun CheckUser(username: String): Boolean {
        val userList = sqliteHelper.GetAllUsers()
        val usernames = userList.map { it.username }
        var repeat = true

        for (u in usernames) {
            if(username.contains(u)) {
                repeat = false
            }
        }

        return repeat

    }

}