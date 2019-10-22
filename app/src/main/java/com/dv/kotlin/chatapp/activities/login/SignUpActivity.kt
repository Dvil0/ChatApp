package com.dv.kotlin.chatapp.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dv.kotlin.chatapp.R
import com.dv.kotlin.chatapp.activities.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        btnGoLogin.setOnClickListener {
            goToActivity<LoginActivity>{ flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            overridePendingTransition( android.R.anim.fade_in, android.R.anim.fade_out )

        }

        btnSignUp.setOnClickListener {

            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()
            val conPass = edtConfirmPass.text.toString()

            if( isValidEmail( email ) && isValidPassword( pass ) && isValidConfirmPassword( pass, conPass )){
                signUpByEmail( email, pass )
            } else {
                toast( "Please make sure all the data is correct." )
            }
        }

        edtEmail.validate {
            edtEmail.error = if( isValidEmail( it )) null else "The email is not valid."
        }
        edtPass.validate {
            edtPass.error = if( isValidPassword( it )) null else "The password should contain 1 lowercase, 1 uppercase, 1 number 1 special character at least."
        }
        edtConfirmPass.validate {
            edtConfirmPass.error = if( isValidConfirmPassword( edtPass.text.toString(), it )) null else "Confirm password doesn't match with password."
        }

    }

    private fun signUpByEmail( email: String, password: String ){
        mAuth.createUserWithEmailAndPassword( email, password )
            .addOnCompleteListener( this ){

                if( it.isSuccessful ){
                    mAuth.currentUser!!.sendEmailVerification().addOnCompleteListener( this ) {
                        toast( "An email has been sent to you. Please, confirm before sign in." )
                        goToActivity<LoginActivity>{ flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                        overridePendingTransition( android.R.anim.fade_in, android.R.anim.fade_out )
                    }
                } else {
                    toast( "An unexpected error occurred. Please, try again." )
                    Log.e( "AUTH", "${it.exception}" )
                }
            }
    }
}
