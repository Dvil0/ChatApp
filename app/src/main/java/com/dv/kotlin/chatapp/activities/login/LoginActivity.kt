package com.dv.kotlin.chatapp.activities.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dv.kotlin.chatapp.R
import com.dv.kotlin.chatapp.activities.*
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity: AppCompatActivity(){

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }

    override fun onCreate( savedInstanceState: Bundle?){
        super.onCreate( savedInstanceState )
        setContentView( R.layout.login_activity )

        btnLogin.setOnClickListener {

            val email = edtEmail.text.toString()
            val pass = edtPass.text.toString()

            if( isValidEmail( email ) && isValidPassword( pass ) ){
                logInByEmail( email, pass )
            } else {
                toast(  "Please make sure all the data is correct." )
            }
        }
        txtForgotPass.setOnClickListener{
            goToActivity<ForgotPasswordActivity>()
            overridePendingTransition( android.R.anim.slide_in_left, android.R.anim.slide_out_right )
        }
        btnSignUp.setOnClickListener {
            goToActivity<SignUpActivity>()
            overridePendingTransition( android.R.anim.slide_in_left, android.R.anim.slide_out_right )
        }
        edtEmail.validate {
            edtEmail.error = if( isValidEmail( it )) null else "The email is not valid."
        }
        edtPass.validate {
            edtPass.error = if( isValidPassword( it )) null else "The password should contain 1 lowercase, 1 uppercase, 1 number 1 special character at least."
        }

    }

    private fun logInByEmail( email: String, pass: String ){
        mAuth.signInWithEmailAndPassword( email, pass )
            .addOnCompleteListener( this ){
                if( it.isSuccessful ){
                    if( mAuth.currentUser!!.isEmailVerified ){
                        toast( "User is now logged in." )
                    } else {
                        toast( "User must confirm email first." )
                    }
                } else {
                    toast("An unexpected error occurred. Try again." )
                }
            }
    }
}