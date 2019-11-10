package com.sinergia.eLibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    //Login Variables References
    private lateinit var login_btn: Button

    private lateinit var login_user: EditText
    private lateinit var login_pass: EditText
    private lateinit var login_forgot_pass: TextView

    private val TAG = "LoginActivity"

    private var email: String?= null
    private var password: String?=null

    //Firebase References
    private var nelsAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initialize()

    }

    fun initialize(){

        //Login Variables
        login_user = findViewById<EditText>(R.id.login_user)
        login_pass = findViewById<EditText>(R.id.login_pass)

        //Firebase Variables
        nelsAuth = FirebaseAuth.getInstance()

        //Forgot Password function variables
        login_forgot_pass = findViewById<TextView>(R.id.login_pass_forgotten)
        login_forgot_pass.setOnClickListener() {
            var forgotPasswordIntent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
            startActivity(forgotPasswordIntent)
        }


        //Login Button and Function
        login_btn = findViewById<Button>(R.id.login_btn)
        login_btn.setOnClickListener(){ login() }

    }

    fun login() {


        //login function variables
        email = login_user?.text.toString()
        password = login_pass?.text.toString()

        if(email!!.isEmpty()){
            Toast.makeText(this, "El nombre de Usuario es un campo obligatorio.", Toast.LENGTH_SHORT).show()
        } else if (password!!.isEmpty()){
            Toast.makeText(this, "La contraseña es un campo obligatorio.", Toast.LENGTH_SHORT).show()
        } else {

            Log.d(TAG, "Login in user.")

            nelsAuth!!.signInWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener(this) {task ->

                    if(task.isSuccessful){
                        Log.d(TAG, "Login with email: Succesfull.")
                        updateUI()
                    } else {
                        Log.e(TAG, "Login with email: Failure.")
                        Toast.makeText(this, "No se ha podido iniciar sesión, revisa tus credenciales y vuelve a intentarlo.", Toast.LENGTH_SHORT).show()
                    }

                }


        }

    }

    fun updateUI() {

        Toast.makeText(this, "AQUÍ SE ENTRARÍA A LA PÁGINA PRINCIPAL", Toast.LENGTH_SHORT).show()
        /*
        val intentLogin = Intent(this, MainPage::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intentLogin)
        */

    }

}
