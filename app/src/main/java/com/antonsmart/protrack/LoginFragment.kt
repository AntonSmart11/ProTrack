package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.navigation.fragment.findNavController

class LoginFragment : Fragment(R.layout.fragment_login) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnRegister = requireView().findViewById<TextView>(R.id.btnRegister)
        val btnEnter = requireView().findViewById<Button>(R.id.btnEnter)

        btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }

        btnEnter.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        }

    }

}