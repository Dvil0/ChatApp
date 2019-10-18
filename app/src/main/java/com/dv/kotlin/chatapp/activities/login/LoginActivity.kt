package com.dv.kotlin.chatapp.activities.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dv.kotlin.chatapp.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity: AppCompatActivity(){

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }

    override fun onCreate( savedInstanceState: Bundle?){
        super.onCreate( savedInstanceState )
        setContentView( R.layout.login_activity )

        var email: String?
        var pass: String?

        btnLogin.setOnClickListener {

            email = edtEmail.text.toString()
            pass = edtPass.text.toString()

            if( isValidateLoginFields( email, pass ) ){
                logIn( email!!, pass!! )
            } else {
                Toast.makeText( this, "Please fill all the data is correct.", Toast.LENGTH_LONG ).show()
            }
        }

        btnSignUp.setOnClickListener {
            val intent = Intent( this, SignUpActivity::class.java )
            startActivity( intent )
        }

    }

    private fun logIn( email: String, pass: String ){
        mAuth.signInWithEmailAndPassword( email, pass )
            .addOnCompleteListener( this ){
                if( it.isSuccessful ){
                    Toast.makeText( this, "Welcome", Toast.LENGTH_LONG ).show()
                } else {
                    Toast.makeText( this, "An unexpected error ocurred. Check the data and try again.", Toast.LENGTH_LONG ).show()
                }
            }
    }

    private fun isValidateLoginFields( email: String?, pass: String? ): Boolean{
        return !email.isNullOrEmpty() &&
                !pass.isNullOrEmpty()
    }
}