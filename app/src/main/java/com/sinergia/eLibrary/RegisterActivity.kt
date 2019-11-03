package com.sinergia.eLibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class RegisterActivity : AppCompatActivity() {

    private lateinit var register_btn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        register_btn = findViewById<Button>(R.id.register_btn)

        register_btn.setOnClickListener(){
            Toast.makeText(this,  "ESTO AUN NO VA TOLAI, NO LE DES MAS",  Toast.LENGTH_LONG).show()
        }

    }
}
