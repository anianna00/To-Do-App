package com.example.to_do.fragments

import android.annotation.SuppressLint
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color.Companion.Cyan
import androidx.compose.ui.graphics.Color.Companion.Magenta
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.Fragment
import com.example.to_do.LoginActivity
import com.example.to_do.R
import com.example.to_do.databinding.FragmentUserBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthRecentLoginRequiredException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class UserFragment: Fragment() {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!
    private var displayResetPasswordDialog by mutableStateOf(false)

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val view = binding.root

        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.setContent {
            if (displayResetPasswordDialog) {
                ChangePasswordDialog(
                    onDismiss = { displayResetPasswordDialog = false },
                    onUpdatePassword = { updatedPassword ->
                        val currentUser = Firebase.auth.currentUser
                        if (currentUser != null) {
                            changePassword(currentUser, updatedPassword)
                        }
                    }
                )
            }
        }

        // wyswietlenie maila uzytkownika
        val sharedPreferences: SharedPreferences = requireContext().getSharedPreferences("UserPreferences", MODE_PRIVATE)
        val userEmail = sharedPreferences.getString("USER_EMAIL", "User")
        val userTextView = view.findViewById<TextView>(R.id.userTextView)
        userTextView.text = "Hello, $userEmail"

        val buttonLogout = view.findViewById<Button>(R.id.button_logout)
        val buttonDeleteAccount: Button = view.findViewById(R.id.button_delete_account)
        val buttonResetPassword: Button = view.findViewById(R.id.button_reset_password)
        val user = Firebase.auth.currentUser!!

        buttonLogout.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()
            editor.apply()
            Firebase.auth.signOut()
            changeToLogin()
        }

        buttonDeleteAccount.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(R.string.are_you_sure)
            builder.setPositiveButton("Delete account"){ _, _ ->
                deleteAccount(user)
            }
            builder.setNeutralButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        buttonResetPassword.setOnClickListener {
            displayResetPasswordDialog = true
        }

        return view
    }

    private fun deleteAccount(user: FirebaseUser){
        user.delete().addOnCompleteListener { task ->
            if (task.isSuccessful){
                Toast.makeText(requireContext(), R.string.account_deleted, LENGTH_SHORT).show()
                Firebase.auth.signOut()
                changeToLogin()
            } else {
                if(task.exception is FirebaseAuthRecentLoginRequiredException){
                    loginAgain()
                } else {
                    Toast.makeText(requireContext(), R.string.delete_error, LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun changePassword(user: FirebaseUser, updatedPassword: String){
        user.updatePassword(updatedPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(context, R.string.password_changed, LENGTH_SHORT).show()
            } else if(task.exception is FirebaseAuthRecentLoginRequiredException){
                loginAgain()
            }
            else {
                Toast.makeText(context, R.string.error, LENGTH_SHORT).show()
            }
        }
    }

    @Composable
    fun ChangePasswordDialog(
        onDismiss: ()-> Unit,
        onUpdatePassword: (String) -> Unit
    ){
        var updatedPassword by remember { mutableStateOf("") }
        var confirmUpdatedPassword by remember { mutableStateOf("") }
        val rainbowColors = listOf(Magenta, Cyan, Magenta, Cyan)

        Dialog(onDismissRequest = onDismiss) {
            Card(
                modifier = Modifier
                    .size(width = 400.dp, height = 300.dp)
                    .height(300.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Update password",
                        fontSize = 24.sp,
                        style = TextStyle(
                            brush = Brush.linearGradient(
                                colors = rainbowColors
                            )
                        ),
                        modifier = Modifier.padding(16.dp)
                    )
                    TextField(
                        value = updatedPassword,
                        onValueChange = { updatedPassword = it },
                        label = {Text("Insert new password")},
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                    )
                    TextField(
                        value = confirmUpdatedPassword,
                        onValueChange = {confirmUpdatedPassword = it},
                        label = {Text("Confirm new password")},
                        visualTransformation = PasswordVisualTransformation()
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.Center
                    ){
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text("Cancel")
                        }
                        Button(
                            onClick = {
                                if(updatedPassword.isNotEmpty() && confirmUpdatedPassword.isNotEmpty()) {
                                    if (updatedPassword == confirmUpdatedPassword) {
                                        onUpdatePassword(updatedPassword)
                                        onDismiss()
                                    } else {
                                        Toast.makeText(context, R.string.passwords_dont_match, LENGTH_SHORT).show()
                                    }
                                }else {
                                    Toast.makeText(
                                        context,
                                        R.string.fillout_all_fields,
                                        LENGTH_SHORT
                                    ).show()
                                }
                            }
                        ) {
                            Text("Update password")
                        }
                    }
                }
            }
        }
    }

    private fun loginAgain(){
        Toast.makeText(requireContext(), R.string.reauthentication_needed, LENGTH_SHORT).show()
        Firebase.auth.signOut()
        changeToLogin()
    }

    private fun changeToLogin(){
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}