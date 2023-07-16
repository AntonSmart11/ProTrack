package com.antonsmart.protrack

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
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

        //BtnSaveUser
        binding.btnSaveUser.setOnClickListener {
            UpdateUser()
        }

        //BtnDeleteUser
        binding.btnDeleteUser.setOnClickListener {
            DeleteUser(Global.idUser)
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun DeleteUser(id: Int) {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.delete_user_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val nextButton = dialog.findViewById<Button>(R.id.btnNextDeleteUser)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelDeleteUser)

        nextButton.setOnClickListener {
            sqliteHelper.DeleteUser(id)
            dialog.dismiss()
            findNavController().navigate(R.id.action_userFragment_to_loginFragment)
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun UpdateUser() {
        val name = binding.userName.text.toString()
        val last = binding.userLast.text.toString()
        val user = binding.userUsername.text.toString()
        val password = binding.userPassword.text.toString()

        if(!name.isEmpty()) {
            if(!last.isEmpty()) {
                if(!user.isEmpty()) {
                    if(!password.isEmpty()) {
                        val user = User(Global.idUser, name, last, user, password)
                        val status = sqliteHelper.UpdateUser(user)

                        if(status > -1) {
                            Toast.makeText(requireContext(), "Actualizado exitosamente", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Acción fallida", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(requireContext(), "Campo contraseña vacío", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Campo usuario vacío", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Campo apellido vacío", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "Campo nombre vacío", Toast.LENGTH_SHORT).show()
        }
    }

    private fun user(id: Int): User {
        val userList = sqliteHelper.GetAllUsers()
        val user = userList.find { it.id == id }

        return user!!
    }

}