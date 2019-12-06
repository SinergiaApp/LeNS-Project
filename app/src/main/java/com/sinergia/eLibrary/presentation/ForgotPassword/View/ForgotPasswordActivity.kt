package com.sinergia.eLibrary.presentation.ForgotPassword.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity

class ForgotPasswordActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initialize()
    }

    override fun getLayout(): Int {
        return R.layout.activity_forgot_password
    }

    override fun getPageTitle(): String {
        return "FORGOT PASSWORD"
    }

    //Login Variables References
    private lateinit var forgot_pass_btn: Button

    private lateinit var forgot_pass_email: EditText

    private var email: String? = null

    private val TAG = "ForgotPasswordActivity"

    //Firebase References
    private var nelsAuth: FirebaseAuth? = null



    //Initialize function
    fun initialize() {

        //Forgot Password Variables
        forgot_pass_email = findViewById<EditText>(R.id.forgot_pass_email)

        //Forgot Password Button
        forgot_pass_btn = findViewById<Button>(R.id.forgot_pass_btn)
        forgot_pass_btn.setOnClickListener{ sendResetPassEmail() }

    }

    //Send Reset Password Email Function
    fun sendResetPassEmail() {

        email = forgot_pass_email.text.toString()

        if(email!!.isEmpty()){
            Toast.makeText(this, "Debe indicar el correo electrónico de inicio de sesión.", Toast.LENGTH_SHORT).show()
        } else {
            nelsAuth!!
                .sendPasswordResetEmail(email!!)
                .addOnCompleteListener{task ->
                    if (task.isSuccessful){
                        Log.d(TAG, "Reset Password Email sent.")
                        Toast.makeText(this, "Se ha enviado el correo de reseteo de contraseña.", Toast.LENGTH_SHORT).show()
                        updateUI()
                    } else {
                        Log.w(TAG, task.exception?.message.toString())
                        Toast.makeText(this, "No hay ningún usuario asociado a ese correo electrónico, por favor revisa las credenciales y vuelve a intentarlo", Toast.LENGTH_LONG).show()
                    }
                }
        }

    }

    fun updateUI() {

        val forgotPasswordIntent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(forgotPasswordIntent)

    }
}
