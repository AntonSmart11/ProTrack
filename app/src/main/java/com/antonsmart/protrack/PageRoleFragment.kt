package com.antonsmart.protrack

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentPageRoleBinding
import com.antonsmart.protrack.global.Global
import com.antonsmart.protrack.objects.Role

class PageRoleFragment : Fragment(R.layout.fragment_page_role) {

    private lateinit var binding: FragmentPageRoleBinding
    private val args: PageRoleFragmentArgs by navArgs()
    private lateinit var sqLiteHelper: SQLiteHelper



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentPageRoleBinding.bind(view)

        sqLiteHelper = SQLiteHelper(requireContext())


        val roleId = args.idRole


        val role = getRole(roleId)


        //Content
        val title = binding.titleRolePage
        val name = binding.roleName

        updatePage(role,name)

        //Buttons
        val btnEdit = binding.editRole
        val btnDelete = binding.deleteRole


        btnEdit.setOnClickListener{
            showEditRoleDialog(roleId,role,name)
        }

        btnDelete.setOnClickListener{
            DeleteRole(roleId)
        }

        binding.arrowBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

    }

    private fun DeleteRole(id: Int){
        val dialog = Dialog(requireContext(),R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.delete_role_alert_dialog,null)
        dialog.setContentView(dialogLayout)

        val nextButton = dialog.findViewById<Button>(R.id.btnNextDeleteRole)
        val cancelButton = dialog.findViewById<Button>(R.id.btnCancelDeleteRole)

        nextButton.setOnClickListener {
            sqLiteHelper.DeleteRole(id)
            dialog.dismiss()
            findNavController().navigate(R.id.action_pageRoleFragment_to_roleFragment)
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun showEditRoleDialog(id: Int, role1: Role, name1: TextView){
        val dialog = Dialog(requireContext(), R.style.CustomAlertDialog)
        val dialogLayout = layoutInflater.inflate(R.layout.role_alert_dialog,null)
        dialog.setContentView(dialogLayout)

        val id_user = Global.idUser
        val nameEditText = dialogLayout.findViewById<EditText>(R.id.nameRoleEditText)

        val role = getRole(id)

        nameEditText.setText(role.name)

        val nextButton = dialog.findViewById<ImageButton>(R.id.nextRoleButton)
        nextButton.setOnClickListener {
            val name = nameEditText.text.toString()

            if(!name.isEmpty()){
                val role = Role(id,Global.idUser,name)
                val status = sqLiteHelper.UpdateRole(role)

                if(status  > -1){
                    Toast.makeText(requireContext(),"Actualizado exitosamente", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()


                    updatePage(role, name1)
                }

            }else {
                Toast.makeText(requireContext(), "Campo nombre vacío", Toast.LENGTH_SHORT).show()
            }
        }

        dialog.show()
    }

        private fun updatePage(role: Role, name: TextView){
        name.setText(role.name)
    }

    private fun getRole(id: Int): Role {
        val roleList = sqLiteHelper.GetAllRoles()
        val role = roleList.find { it.id == id }

        return role!!
    }

}

