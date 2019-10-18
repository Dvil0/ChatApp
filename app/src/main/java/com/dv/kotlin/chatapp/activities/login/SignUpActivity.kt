package com.dv.kotlin.chatapp.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.dv.kotlin.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnGoLogin.setOnClickListener {
            val intent = Intent( this, LoginActivity::class.java )
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity( intent )
        }

        btnSignUp.setOnClickListener {

            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()

            if( isValidEmailAndPassword( email, pass ) ){

                signUpByEmail( email, pass )
            } else {
                Toast.makeText( this, "Please fill all the data and confirm password is correct.", Toast.LENGTH_LONG ).show()
            }
        }
    }

    private fun signUpByEmail( email: String, password: String ){
        mAuth.createUserWithEmailAndPassword( email, password )
            .addOnCompleteListener( this ){

                if( it.isSuccessful ){
                    Toast.makeText( this, "An email has been sent to you. Please, confirm before sign in.", Toast.LENGTH_LONG ).show()
                } else {
                    Toast.makeText( this, "An unexpected error ocurred. Please, try again.", Toast.LENGTH_LONG ).show()
                    Log.e( "AUTH", "${it.exception}" )
                }
            }
    }

    private fun isValidEmailAndPassword( email: String?, password: String? ): Boolean{
        return !email.isNullOrEmpty() &&
                !password.isNullOrEmpty() &&
                password == edtConfirmPass.text.toString()
    }
}
