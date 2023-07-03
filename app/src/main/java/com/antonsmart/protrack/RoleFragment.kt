package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.RoleAdapter
import com.antonsmart.protrack.databinding.FragmentRoleBinding
import com.antonsmart.protrack.objects.Role


class RoleFragment : Fragment(R.layout.fragment_role) {

    private lateinit var binding: FragmentRoleBinding
    private var listRoles: MutableList<Role> = mutableListOf()
    private lateinit var recycler: RecyclerView

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        listRoles.add(Role("Rol 1"))
        listRoles.add(Role("Rol 2"))
        listRoles.add(Role("Rol 3"))
        listRoles.add(Role("Rol 4"))
        listRoles.add(Role("Rol 5"))

        setAdapater()

        binding.addRole.setOnClickListener{
            showCreateProjectDialog()
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    private fun setAdapater(){
        recycler = requireView().findViewById(R.id.roleRecyclerView)
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.adapter = RoleAdapter(requireContext(),listRoles)
    }

    private fun showCreateProjectDialog(){
        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.role_alert_dialog,null)
        dialog.setContentView(dialogLayout)

        val nextBottom = dialog.findViewById<ImageButton>(R.id.nextRoleButton)
        nextBottom.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}