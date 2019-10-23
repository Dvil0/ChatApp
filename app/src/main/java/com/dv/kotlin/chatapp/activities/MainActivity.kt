package com.dv.kotlin.chatapp.activities

import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.dv.kotlin.chatapp.R
import com.dv.kotlin.mylibraryaar.ToolbarActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity: ToolbarActivity(){

    private val mAuth: FirebaseAuth by lazy{ FirebaseAuth.getInstance() }

    override fun onCreate( savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        toolbarToLoad( toolbarView as Toolbar )

    }
}