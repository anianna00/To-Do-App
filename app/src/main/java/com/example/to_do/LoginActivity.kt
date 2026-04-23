package com.example.to_do

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.fragments.EmailDialogFragment
import com.example.to_do.fragments.TaskDialogFragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.auth
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    private val auth: FirebaseAuth = Firebase.auth
    private lateinit var emailEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var emailDialogFragment: EmailDialogFragment

    // czy uzytkownik jest juz zalogowany
    override fun onStart() {
        super.onStart()

        val justSignedUp = intent.getBooleanExtra("JUST_SIGNED_UP", false)

        if(!justSignedUp){
            val currentUser = auth.currentUser
            if (currentUser != null) {
                changeToHome()
            }
        } else{
            intent.removeExtra("JUST_SIGNED_UP")
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEdit = findViewById(R.id.email_edit_text)
        passwordEdit = findViewById(R.id.password_edit_text)
        val buttonLogin = findViewById<Button>(R.id.button_login)
        val signupLink: TextView = findViewById(R.id.signup_link)
        val resetPasswordLink : TextView = findViewById(R.id.forgotten_password_link)

        buttonLogin.setOnClickListener {
            val email = emailEdit.text.toString()
            val password = passwordEdit.text.toString()
            if(email.isNotEmpty() && password.isNotEmpty()){
                login(email, password)
            } else {
                makeToast(this, R.string.fillout_all_fields)
            }

        }

        signupLink.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignupActivity::class.java)
            startActivity(intent)
        }

        resetPasswordLink.setOnClickListener {
            emailDialogFragment = EmailDialogFragment.newInstance(emailEdit.text.toString())
            emailDialogFragment.show(supportFragmentManager, "email_dialog")
        }

    }

    private fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
            if (task.isSuccessful){
                // czy email jest potwierdzony
                if (auth.currentUser?.isEmailVerified == true) {
                    makeToast(this, R.string.login_successful)
                    val sharedPreferences: SharedPreferences = getSharedPreferences("UserPreferences", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("USER_EMAIL", email)
                    editor.apply()

                    changeToHome()
                } else {
                    makeToast(this, R.string.confirm_email)
                }
            } else if(task.exception is FirebaseAuthInvalidCredentialsException){
                makeToast(this, R.string.invalid_credentials)
            }
            else {
                makeToast(this, R.string.login_error)
            }
        }

    }

    private fun changeToHome() {
        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun makeToast(context: Context, message: Int){
        Toast.makeText(context, message, LENGTH_SHORT).show()
    }
}