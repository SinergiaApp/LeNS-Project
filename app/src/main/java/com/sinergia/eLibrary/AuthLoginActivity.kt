package com.sinergia.eLibrary

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class AuthLoginActivity : AppCompatActivity() {

    lateinit var providers: List<AuthUI.IdpConfig>

    private lateinit var login_btn: Button
    private lateinit var logout_btn: Button

    val RC_LoginGoogle: Int = 8888

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth_login)

        //Login and Logout Buttons
        login_btn = findViewById<Button>(R.id.login_btn)
        logout_btn = findViewById<Button>(R.id.logout_btn)

        providers = Arrays.asList<AuthUI.IdpConfig>(

            //Login with email
            AuthUI.IdpConfig.EmailBuilder().build(),
            //Login with Google
            AuthUI.IdpConfig.GoogleBuilder().build()

        )

        showSignInOptions()


        //EVENT --> Click on Logout Button
        logout_btn.setOnClickListener {

            AuthUI.getInstance().signOut(this)
                .addOnCompleteListener {
                    logout_btn.isEnabled = false
                    showSignInOptions()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
        }

        fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

            super.onActivityResult(requestCode, resultCode, data)
            if (requestCode == RC_LoginGoogle) {

                val response = IdpResponse.fromResultIntent(data)
                if (resultCode == Activity.RESULT_OK) {
                    val user = FirebaseAuth.getInstance().currentUser
                    Toast.makeText(this, "" + user!!.email, Toast.LENGTH_SHORT).show()

                    logout_btn.isEnabled = true
                } else {
                    Toast.makeText(this, "" + response!!.error!!.message, Toast.LENGTH_SHORT).show()
                }

            }

        }


    }

    //showSignInOptions function for Login
    private fun showSignInOptions() {
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTheme(R.style.LoginTheme)
                .build(), RC_LoginGoogle
        )
    }

}