package com.antonsmart.protrack

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings.Global
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.antonsmart.protrack.database.SQLiteHelper
import com.antonsmart.protrack.databinding.FragmentLoginBinding
import com.antonsmart.protrack.objects.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginFragment : Fragment(R.layout.fragment_login) {

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var sqliteHelper: SQLiteHelper
    private lateinit var auth: FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = DataBindingUtil.bind(view)!!

        sqliteHelper = SQLiteHelper(requireContext())

        val sharedPrefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val sessionActive = sharedPrefs.getBoolean("session_active", false)

        if(sessionActive) {
            findNavController().navigate(R.id.action_loginFragment_to_dashboardFragment)
        }

        binding.btnEnter.setOnClickListener {
            val username = binding.loginUser.text.toString()
            val password = binding.loginPassword.text.toString()

            if(username.isEmpty()) {
                Toast.makeText(context, "Campo usuario vacío", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                Toast.makeText(context, "Campo contraseña vacío", Toast.LENGTH_SHORT).show()
            } else {

                if(ValidationUsername(username)) {
                    if(ValidationPassword(username, password))  {
                        Toast.makeText(context, "Iniciado exitosamente", Toast.LENGTH_SHORT).show()

                        val id_user = UserId(username).id

                        SessionActive(id_user)

                        val direction = LoginFragmentDirections.actionLoginFragmentToDashboardFragment(id_user)
                        findNavController().navigate(direction)

                    } else {
                        Toast.makeText(context, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Usuario inexistente", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment2)
        }

        //Google Autentication

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser

        if (currentUser != null) {

        }

        binding.googleBtn.setOnClickListener {
            singInGoogle()
        }
    }

    // Google
    private fun singInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(requireContext(), "Inicio de sesión fallido", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireContext(), "Iniciado cómo ${user?.displayName}", Toast.LENGTH_SHORT).show()

                    //Change of screen here

                } else {
                    Toast.makeText(requireContext(), "Autenticación fallida", Toast.LENGTH_SHORT).show()
                }
            }
    }

    //End of Google

    private fun SessionActive(id: Int) {
        val sharedPrefs = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        editor.putBoolean("session_active", true)
        editor.putInt("id_user", id)
        editor.apply()
    }

    private fun UserId(username: String): User {
        val userList = sqliteHelper.GetAllUsers()
        val user = userList.find { it.username == username }

        return user!!
    }

    private fun ValidationPassword(username: String, password: String): Boolean {
        val userList = sqliteHelper.GetAllUsers()
        val user = userList.find { it.username == username }
        var right = false

        if(user!!.password == password) {
            right = true
        }

        return right
    }

    private fun ValidationUsername(username: String): Boolean {
        val userList = sqliteHelper.GetAllUsers()
        val usernames = userList.map { it.username }
        var exists = false

        for (u in usernames) {
            if(username.contains(u)) {
                exists = true
            }
        }

        return exists
    }

}