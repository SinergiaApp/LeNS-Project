package com.sinergia.eLibrary.presentation.Main.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.presentation.Login.View.LoginActivity
import com.sinergia.eLibrary.presentation.Register.View.RegisterActivity

class MainActivity : AppCompatActivity() {

    private lateinit var login_btn: Button
    private lateinit var register_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_btn = findViewById<Button>(R.id.main_login_btn)
        register_btn = findViewById<Button>(R.id.register_btn)


        login_btn.setOnClickListener(){
            val intent_login = Intent(this, LoginActivity::class.java)
            startActivity(intent_login)
        }


        register_btn.setOnClickListener(){

            val intent_register = Intent(this, RegisterActivity::class.java)
            startActivity(intent_register)

        }
    }
}
