package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class RegisterFragment : Fragment(R.layout.fragment_register) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnLogin = requireView().findViewById<TextView>(R.id.btnLogin)
        val btnRegister = requireView().findViewById<Button>(R.id.btnRegister)

        btnLogin.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment)
        }

        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment2_to_loginFragment)
        }
    }

}