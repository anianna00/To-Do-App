package com.example.to_do.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.DialogFragment
import com.example.to_do.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.auth

class EmailDialogFragment: DialogFragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_email_dialog, container, false)

        val auth = Firebase.auth
        val emailEditText: EditText = view.findViewById(R.id.email_dialog_email)
        val cancelButton: Button = view.findViewById(R.id.email_dialog_cancel)
        val sendButton: Button = view.findViewById(R.id.email_dialog_send)

        sendButton.setOnClickListener {
            val email = emailEditText.text.toString()
            if(email.isNotEmpty()){
                auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        // tylko jesli jest polaczony z kontekstem
                        context?.let{
                            Toast.makeText(requireContext(), "Email został wysłany", LENGTH_SHORT).show()
                        }
                        Log.d("Email Dialog Fragment", "email sent")
                    }
                    else {
                        context?.let{
                            Log.d("Email Dialog Fragment", "email couldn't be sent")
                        }
                    }
                }
                dismiss()
            } else {
                Toast.makeText(requireContext(), "uzupełnij pole email", LENGTH_SHORT).show()
            }

        }

        cancelButton.setOnClickListener {
            dismiss()
        }


        return view
    }

    companion object{
        fun newInstance(email: String): EmailDialogFragment{
            val fragment = EmailDialogFragment()
            val args = Bundle().apply {
                putString("email", email)
            }
            fragment.arguments = args
            return fragment
        }
    }
}