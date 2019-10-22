package com.dv.kotlin.chatapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dv.kotlin.chatapp.R
import com.google.firebase.auth.FirebaseAuth

class MainActivity: AppCompatActivity(){

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }

    override fun onCreate( savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        mAuth.signOut()
    }
}