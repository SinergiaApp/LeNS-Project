package com.sinergia.eLibrary

//import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : AppCompatActivity() {

    //Register Variables References
    private lateinit var register_name: EditText
    private lateinit var register_lastname: EditText
    private lateinit var register_username: EditText
    private lateinit var register_userpass1: EditText
    private lateinit var register_userpass2: EditText
    private lateinit var register_progressBar: ProgressBar
    private lateinit var register_btn: Button

    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null
    private var repeatPassword: String? = null

    private val TAG = "CreateAccountActivity"

    //Firebase References
    private var nelsDatabaseReference: DatabaseReference? = null
    private var nelsDatabase: FirebaseDatabase? = null
    private var nelsAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        initialize()

    }

    //Function to initialized all variables
    fun initialize(){

        //UI variables
        register_name = findViewById<EditText>(R.id.register_name)
        register_lastname = findViewById<EditText>(R.id.register_lastname)
        register_username = findViewById<EditText>(R.id.register_username)
        register_userpass1 = findViewById<EditText>(R.id.register_pass)
        register_userpass2 = findViewById<EditText>(R.id.register_repeatpass)
        register_progressBar = findViewById<ProgressBar>(R.id.register_progressBar)

        //Register Function Variables
        firstName = register_name?.text.toString()
        lastName = register_lastname?.text.toString()
        email = register_username?.text.toString()
        password = register_pass?.text.toString()
        repeatPassword = register_userpass2?.text.toString()

        //Firebase Variables
        nelsDatabase = FirebaseDatabase.getInstance()
        nelsDatabaseReference = nelsDatabase!!.reference!!.child("Users")
        nelsAuth = FirebaseAuth.getInstance()


        //Register Button and function
        register_btn = findViewById<Button>(R.id.register_btn)

        register_btn.setOnClickListener(){ createNewAcount() }

    }

    //Function to create new Account
    fun createNewAcount(){

        register_progressBar.visibility=ProgressBar.VISIBLE

        //Register process
        if(firstName.isNullOrEmpty() || lastName.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || repeatPassword.isNullOrEmpty()){
            Toast.makeText(this, "Todos los campos son obligatorios, por favor completa el formulario de Nuevo Usuario", Toast.LENGTH_SHORT).show()
            register_progressBar.visibility=ProgressBar.INVISIBLE
        } else {

            if(password != repeatPassword){
                Toast.makeText(this, "Las contraseñas no coinciden, por favor vuelve a escribirlas", Toast.LENGTH_SHORT).show()
                register_progressBar.visibility=ProgressBar.INVISIBLE
            } else {
                Toast.makeText(this, FirebaseAuth.getInstance().currentUser.toString(), Toast.LENGTH_LONG).show()
                nelsAuth!!
                    .createUserWithEmailAndPassword(email!!, password!!)
                    .addOnCompleteListener(this) {task ->

                            if(task.isSuccessful){

                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success")
                                val userId = nelsAuth!!.currentUser!!.uid
                                Toast.makeText(this, "Creando Usuario, por favor espera...", Toast.LENGTH_SHORT).show()

                                //Verify Email
                                verifyEmail()

                                //Update user profile information
                                val currentUserDb = nelsDatabaseReference!!.child(userId)
                                currentUserDb.child("Nombre").setValue(firstName)
                                currentUserDb.child("Apellidos").setValue(lastName)
                                currentUserDb.child("Email").setValue(email)
                                currentUserDb.child("Contraseña").setValue(password)
                                updateUserInfoAndUI()

                                register_progressBar.visibility=ProgressBar.INVISIBLE

                            } else {

                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                Toast.makeText(this, "Fallo de autenticación, intentalo más tarde.", Toast.LENGTH_SHORT).show()

                                register_progressBar.visibility=ProgressBar.INVISIBLE

                            }

                        }

            }

        }
    }

    //Function to update user info and UI
    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    //Function to verify New User email
    private fun verifyEmail() {

        val newUser = nelsAuth!!.currentUser

        newUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this,
                        "Verification email sent to " + newUser.getEmail(),
                        Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(this,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}
