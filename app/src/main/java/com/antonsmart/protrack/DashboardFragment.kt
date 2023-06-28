package com.antonsmart.protrack

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var drawerToggle: ActionBarDrawerToggle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar = view.findViewById(R.id.toolbar)

        toolbar.title = ""

        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val drawerLayout = view.findViewById<DrawerLayout>(R.id.drawerLayout)
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

        val navigationView = view.findViewById<NavigationView>(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation item clicks here
            when (menuItem.itemId) {
                R.id.project -> {
                    Navigation.findNavController(requireView())
                        .navigate(R.id.action_dashboardFragment_to_projectFragment)
                }
                R.id.notes -> {
                    Navigation.findNavController(requireActivity(), R.id.loginFragment)
                        .navigate(R.id.loginFragment)
                }
                R.id.roles -> {
                    Navigation.findNavController(requireActivity(), R.id.loginFragment)
                        .navigate(R.id.loginFragment)
                }
                R.id.remembers -> {
                    Navigation.findNavController(requireActivity(), R.id.loginFragment)
                        .navigate(R.id.loginFragment)
                }
            }
            true
        }

    }

}