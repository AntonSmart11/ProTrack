package com.antonsmart.protrack

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentUserBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.User
import com.google.firebase.auth.FirebaseAuth

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
        binding.userUsername.setText(user.email)
        binding.userPassword.setText(user.password)
        binding.userProvider.setText(user.provider)

        if(binding.userProvider.text.toString() == "firebase") {
            binding.userPassword.isEnabled = false
        }

        //BtnSaveUser
        binding.btnSaveUser.setOnClickListener {
            UpdateUser()
        }

        //BtnDeleteUser
        binding.btnDeleteUser.setOnClickListener {
            DeleteUser(Global.idUser)
            if(binding.userProvider.text.toString() == "firebase") {
                DeleteUserGoogle()
            }
        }

        binding.arrowBack.setOnClickListener {
            val navController = Navigation.findNavController(view)
            navController.navigateUp()
        }
    }

    private fun DeleteUserGoogle() {
        val user = FirebaseAuth.getInstance().currentUser

        user?.delete()
    }

    private fun DeleteUser(id: Int) {
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.delete_user_alert_dialog, null)
        dialog.setContentView(dialogLayout)

        val nextButton = dialog.findViewById<Button>(R.id.btnNextDeleteUser)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelDeleteUser)

        nextButton.setOnClickListener {
            sqliteHelper.DeleteWorkUser(id)
            sqliteHelper.DeleteRoleUser(id)
            sqliteHelper.DeleteProjectUser(id)
            sqliteHelper.DeleteUser(id)
            dialog.dismiss()
            val sharedPrefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
            val editor = sharedPrefs.edit()
            editor.putBoolean("session_active", false)
            editor.putInt("id_user", 0)
            editor.apply()
            Global.idUser = 0
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
        val provider = binding.userProvider.text.toString()

        if(!name.isEmpty()) {
            if(!last.isEmpty()) {
                if(!user.isEmpty()) {
                    if(!password.isEmpty() || provider == "firebase") {
                        val user = User(Global.idUser, name, last, user, password, provider)
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