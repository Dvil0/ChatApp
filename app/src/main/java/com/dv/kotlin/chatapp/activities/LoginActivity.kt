package com.dv.kotlin.chatapp.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dv.kotlin.chatapp.R

class LoginActivity: AppCompatActivity(){

    override fun onCreate( savedInstanceState: Bundle?){
        super.onCreate( savedInstanceState )
        setContentView( R.layout.login_activity )
    }
}