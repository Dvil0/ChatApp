package com.dv.kotlin.chatapp.activities.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dv.kotlin.chatapp.R
import com.dv.kotlin.chatapp.activities.goToActivity
import com.dv.kotlin.chatapp.activities.isValidEmail
import com.dv.kotlin.chatapp.activities.toast
import com.dv.kotlin.chatapp.activities.validate
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : AppCompatActivity() {

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        edtEmail.validate {
            edtEmail.error = if( isValidEmail( edtEmail.text.toString()) ) null else "Email is not valid."
        }

        btnGoLogin.setOnClickListener {
            goToActivity<LoginActivity>{ flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
            overridePendingTransition( android.R.anim.fade_in, android.R.anim.fade_out )

        }

        btnForgot.setOnClickListener{
            val email = edtEmail.text.toString()
            if( isValidEmail( email ) ){
                mAuth.sendPasswordResetEmail( email ).addOnCompleteListener(this){
                    toast( "Email has been sent to reset your password." )
                    goToActivity<LoginActivity>{ flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK }
                    overridePendingTransition( android.R.anim.fade_in, android.R.anim.fade_out )
                }
            } else {
                toast( "Please make sure the email address is correct." )
            }
        }
    }
}
