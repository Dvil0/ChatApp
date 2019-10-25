package com.dv.kotlin.chatapp.activities.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dv.kotlin.chatapp.R
import com.dv.kotlin.chatapp.activities.*
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.android.synthetic.main.login_activity.*

class LoginActivity: AppCompatActivity(){

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }
    private val mGoogleApiClient: GoogleSignInClient by lazy{ getGoogleSignInClient() }
    private val RC_GOOGLE_SIGN_IN = 5

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

        btnLoginGoogle.setOnClickListener {
            val signInIntent: Intent = mGoogleApiClient.signInIntent
            startActivityForResult( signInIntent, RC_GOOGLE_SIGN_IN )
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
                        goToActivity<MainActivity>{
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                    } else {
                        toast( "User must confirm email first." )
                    }
                } else {
                    toast("An unexpected error occurred. Try again." )
                }
            }
    }

    private fun getGoogleSignInClient(): GoogleSignInClient{
        val gso = GoogleSignInOptions.Builder( GoogleSignInOptions.DEFAULT_SIGN_IN )
            .requestEmail()
            .requestIdToken("721168008737-5la5lk2up4da7fuom88c1vlb3d836c7j.apps.googleusercontent.com")
            .build()

        return GoogleSignIn.getClient( this, gso )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if( requestCode == RC_GOOGLE_SIGN_IN ){

            val task = GoogleSignIn.getSignedInAccountFromIntent( data )
            task.addOnCompleteListener {
                val account = task.getResult( ApiException::class.java )
                logInByGoogleAccountIntoFireBase( account!! )
            }
        }
    }

    private fun logInByGoogleAccountIntoFireBase( gAccount: GoogleSignInAccount ){
        val credential = GoogleAuthProvider.getCredential( gAccount.idToken, null )
        mAuth.signInWithCredential( credential )
            .addOnCompleteListener {
                val signIn = GoogleSignIn.getLastSignedInAccount( this )
                if( signIn != null ){
                    getGoogleSignInClient().signOut()
                }
                goToActivity<MainActivity>{
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
            }
    }
}