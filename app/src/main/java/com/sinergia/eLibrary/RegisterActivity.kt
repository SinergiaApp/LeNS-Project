package com.sinergia.eLibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.lang.ref.PhantomReference

class RegisterActivity : AppCompatActivity() {

    private lateinit var txtName: EditText
    private lateinit var txtLastName: EditText
    private lateinit var txtEmail: EditText
    private lateinit var txtPass: EditText
    private lateinit var progressBar: ProgressBar
    private lateinit var dbReference: DatabaseReference
    private lateinit var database:FirebaseDatabase
    private lateinit var auth: FirebaseAuth

    val RC_SIGN_IN: Int = 1
    lateinit var mGoogleSignInClient: GoogleSignInClient
    lateinit var mGoogleSignInOptions: GoogleSignInOptions


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        txtName = findViewById<EditText>(R.id.userRegisterName)
        txtLastName = findViewById<EditText>(R.id.userRegisterLastName)
        txtEmail = findViewById<EditText>(R.id.userRegisterMail)
        txtPass = findViewById<EditText>(R.id.userRegisterPass)

        progressBar = findViewById<ProgressBar>(R.id.RegisterProgressBar)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        dbReference = database.reference.child("Users")



    }

    fun sigIn(view: View) {
        createNewAccount()
    }

    private fun createNewAccount(){

        val name:String=txtName.text.toString()
        val lastName:String=txtLastName.text.toString()
        val email:String=txtEmail.text.toString()
        val pass:String=txtPass.text.toString()

        if(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(lastName) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {

            progressBar.visibility = View.VISIBLE

            auth
                .createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this) { task ->
                    if (task.isComplete) {
                        var user: FirebaseUser? = auth.currentUser
                        verifyEmail(user)

                        val userbd=dbReference.child(user?.uid)

                        userbd.child("Nombre").setValue(name)
                        userbd.child("Apellido").setValue(lastName)
                        action()

                    }
                }


        }
    }
    private fun action(){
        startActivity(Intent(this, LoginActivity::class.java))
    }
    private fun verifyEmail(user:FirebaseUser){

        user
            .sendEmailVerification()
            .addOnCompleteListener(this){
                task -> if(task.isComplete){
                    Toast.makeText(this, "Correo de Verificación Enviado", Toast.LENGTH_LONG).show()
                } else {
                Toast.makeText(this, "Error al enviar Correo de Verificación", Toast.LENGTH_LONG).show()
                }
            }

    }
}
