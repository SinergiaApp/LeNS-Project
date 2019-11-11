package com.sinergia.eLibrary.presentation.Main.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.presentation.Login.View.LoginActivity
import com.sinergia.eLibrary.presentation.Register.View.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var login_btn: Button
    private lateinit var register_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_login_btn.setOnClickListener { startActivity(Intent(this, LoginActivity::class.java)) }
        main_register_btn.setOnClickListener {startActivity(Intent(this, RegisterActivity::class.java ) ) }

    }
}
