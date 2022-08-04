package com.example.socketchat.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.socketchat.R
import com.example.socketchat.presentation.authorization.AuthorizationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, AuthorizationFragment())
            .commit()
    }
}