package com.antonsmart.protrack

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.antonsmart.protrack.databinding.FragmentDashboardBinding
import com.antonsmart.protrack.global.Global
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var mAuth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        //Toolbar
        toolbar = binding.toolbar

        toolbar.title = ""

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Asignation of user in the global variable
        val sharedPrefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val userId = sharedPrefs.getInt("id_user", 0)
        Global.idUser = userId

        //Creation of the drawer layout
        val drawerLayout = binding.drawerLayout
        drawerToggle = ActionBarDrawerToggle(
            requireActivity(),
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerToggle.setHomeAsUpIndicator(R.drawable.menu)
        drawerToggle.drawerArrowDrawable.color = ContextCompat.getColor(requireContext(), R.color.white)
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val navigationView = binding.navigationView
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation item clicks here
            when (menuItem.itemId) {
                R.id.project -> {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_dashboardFragment_to_projectFragment)
                }
                R.id.notes -> {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_dashboardFragment_to_note1Fragment)
                }
                R.id.roles -> {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_dashboardFragment_to_role1Fragment)
                }
                R.id.remembers -> {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_dashboardFragment_to_reminder1Fragment)
                }
            }
            true
        }

        // Google

        mAuth = FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val btnLogout = binding.navigationView.getHeaderView(0).findViewById<Button>(R.id.btnLogout)

        btnLogout.setOnClickListener {
            Global.idUser = 0

            SignOutGoogle()
            SessionLogout()

            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }


        //Content

        binding.projectWidget.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_projectFragment)
        }
        binding.projectImage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_projectFragment)
        }

        binding.rolesWidget.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_role1Fragment)
        }
        binding.rolesImage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_role1Fragment)
        }

        binding.reminderWidget.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_reminder1Fragment)
        }
        binding.reminderImage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_reminder1Fragment)
        }

        binding.noteWidget.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_note1Fragment)
        }
        binding.noteImage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_note1Fragment)
        }

        binding.userButton.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_userFragment)
        }

    }

    private fun SignOutGoogle() {
        mAuth.signOut()
        mGoogleSignInClient.signOut()
    }

    private fun SessionLogout() {
        val sharedPrefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("session_active", false)
        editor.putInt("id_user", 0)
        editor.apply()
    }

}