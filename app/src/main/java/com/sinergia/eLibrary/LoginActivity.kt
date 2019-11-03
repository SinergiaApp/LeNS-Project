package com.sinergia.eLibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    private lateinit var login_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_btn = findViewById<Button>(R.id.login_btn)

        login_btn.setOnClickListener(){

            Toast.makeText(this, "No lo intentes más pesadilla, que este botón no va aún.", Toast.LENGTH_LONG).show()

        }

    }
}
