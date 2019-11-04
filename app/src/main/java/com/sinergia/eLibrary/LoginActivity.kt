package com.sinergia.eLibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import java.util.*

class LoginActivity : AppCompatActivity() {

    private lateinit var login_btn: Button

    private lateinit var login_user: EditText
    private lateinit var login_pass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Login Variables
        login_user = findViewById<EditText>(R.id.login_user)
        login_pass = findViewById<EditText>(R.id.login_pass)


        //Login Button and Function
        login_btn = findViewById<Button>(R.id.login_btn)

        login_btn.setOnClickListener(){

            if(login_user.text.isNullOrEmpty()){
                Toast.makeText(this, "Debe introducir un Nombre de Usuario.", Toast.LENGTH_LONG).show()
            } else if (login_pass.text.isNullOrEmpty()){
                Toast.makeText(this, "Debe introducir una Contraseña.", Toast.LENGTH_LONG).show()
            } else {

            }



            Toast.makeText(this, "No lo intentes más pesadilla, que este botón no va aún.", Toast.LENGTH_LONG).show()
        }

    }
}
