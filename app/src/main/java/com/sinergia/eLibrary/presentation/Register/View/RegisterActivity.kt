package com.sinergia.eLibrary.presentation.Register.View

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Login.View.LoginActivity
import com.sinergia.eLibrary.presentation.Register.RegisterContract
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : BaseActivity(), RegisterContract.RegisterView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_activity)

        register_btn.setOnClickListener{ register() }
    }

    override fun showError(error: String) {
        toast(this, error, "s")
    }

    override fun showMessage(message: String) {
        toast(this, message, "s")
    }

    override fun showProgressBar() {
        register_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        register_progressBar.visibility = View.INVISIBLE
    }

    override fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun register() {

        var name = register_name.text.toString().trim()
        val lastname = register_lastname.toString().trim()
        val email = register_email.text.toString().trim()
        val password = register_password.text.toString().trim()
        val repeatPassword = register_repeatpassword.text.toString().trim()



    }


    /*
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

    }*/
}
