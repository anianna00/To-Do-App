package com.example.to_do

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.auth
import kotlin.math.sign

class SignupActivity: AppCompatActivity() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        emailEditText = findViewById(R.id.signup_email)
        passwordEditText = findViewById(R.id.signup_password)
        confirmPasswordEditText = findViewById(R.id.signup_confirm_password)
        val signupButton: Button = findViewById(R.id.button_signup)

        auth = Firebase.auth

        signupButton.setOnClickListener {
            signUpUser()
        }

        val loginLink: TextView = findViewById(R.id.login_link)

        loginLink.setOnClickListener {
            val intent = Intent(this@SignupActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signUpUser(){
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val confirmPassword = confirmPasswordEditText.text.toString()

        if (email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()){
            if(password == confirmPassword){
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{ signUp ->
                    if (signUp.isSuccessful){
                        Toast.makeText(this, R.string.signup_successful, LENGTH_SHORT).show()
                        changeToLogin()
                        sendEmail()
                    } else if(signUp.exception is FirebaseAuthUserCollisionException) {
                        Toast.makeText(this, "Konto na ten email już istnieje", LENGTH_SHORT).show()
                    } else if(signUp.exception is FirebaseAuthWeakPasswordException){
                        Toast.makeText(this, "Hasło musi miec co najmniej 6 znakow", LENGTH_SHORT).show()
                        passwordEditText.text.clear()
                        confirmPasswordEditText.text.clear()
                    } else {
                        Toast.makeText(this, R.string.signup_unsuccessful, LENGTH_SHORT).show()
                        emailEditText.text.clear()
                        passwordEditText.text.clear()
                        confirmPasswordEditText.text.clear()
                    }
                }
            } else {
                Toast.makeText(this, R.string.passwords_dont_match, LENGTH_SHORT).show()
            }
        } else{
            Toast.makeText(this, R.string.fillout_all_fields, LENGTH_SHORT).show()
        }

    }

    private fun changeToLogin(){
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        intent.putExtra("JUST_SIGNED_UP", true)
        startActivity(intent)
        finish()
    }

    private fun sendEmail(){
        val user = auth.currentUser
        user!!.sendEmailVerification().addOnCompleteListener { task ->
            if(task.isSuccessful){
                Log.d(TAG, "Verification email sent")
            }
        }
    }
}
