package com.example.socketchat.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socketchat.R
import com.example.socketchat.presentation.authorization.AuthorizationFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.container, AuthorizationFragment())
                .commit()
        }
    }
}