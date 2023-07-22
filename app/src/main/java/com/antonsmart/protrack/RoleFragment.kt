package com.antonsmart.protrack

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.antonsmart.protrack.adapters.RoleAdapter
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentRoleBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Role


class RoleFragment : Fragment(R.layout.fragment_role) {

    private lateinit var binding: FragmentRoleBinding
    private var listRoles: MutableList<Role> = mutableListOf()
    private lateinit var recycler: RecyclerView
    private lateinit var sqliteHelper: SQLiteHelper


    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqliteHelper = SQLiteHelper(requireContext())

        getRoles(Global.idUser)

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

        val id_user = Global.idUser
        val roleEditText = dialogLayout.findViewById<EditText>(R.id.nameRoleEditText)

        val nextBottom = dialog.findViewById<ImageButton>(R.id.nextRoleButton)
        nextBottom.setOnClickListener {

            val name = roleEditText.text.toString()

            val success = addRole(id_user,name)

            if (success){
                getRoles(Global.idUser)
                recycler.adapter?.notifyDataSetChanged()
                dialog.dismiss()
            }

        }

        dialog.show()
    }

    private fun addRole(id_user: Int, name: String) : Boolean{
        var success = false

        if(id_user != 0){
            if(!name.isEmpty()){
                val role = Role(0,id_user, name)
                val status = sqliteHelper.InsertRole(role)

                if (status > -1){
                    success = true
                    Toast.makeText(context, "Agregado exitosamente", Toast.LENGTH_SHORT).show()
                }

            }else{
                Toast.makeText(context, "Campo nombre vacío", Toast.LENGTH_SHORT).show()
            }
        }

        return success
    }

    private fun getRoles(id: Int){
        val roleList = sqliteHelper.GetAllRoles()
        val roles = roleList.filter { it.id_user == id }

        listRoles.clear()

        for (role in roles){
            listRoles.add(Role(role.id,role.id_user,role.name))
        }
    }
}