package com.sinergia.eLibrary.presentation.Register.View

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.domain.interactors.RegisterInteractor.RegisterInteractorImpl
import com.sinergia.eLibrary.presentation.MainPage.View.MainPage
import com.sinergia.eLibrary.presentation.Register.Presenter.RegisterPresenter
import com.sinergia.eLibrary.presentation.Register.RegisterContract
import kotlinx.android.synthetic.main.register_activity.*

class RegisterActivity : BaseActivity(), RegisterContract.RegisterView {

    //PRESENTER INITIALIZATION
    lateinit var presenter: RegisterPresenter

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = RegisterPresenter(RegisterInteractorImpl())
        presenter.attachView(this)

        register_btn.setOnClickListener { register() }
    }

    override fun getLayout(): Int {
        return R.layout.register_activity
    }


    //REGISTER CONTRACT METHODS
    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, MainPage::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }

    override fun navigateToRegister() {
        val intentMainPage = Intent(this, RegisterActivity::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }

    override fun showError(error: String) {
        toastS(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showProgressBar() {
        register_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        register_progressBar.visibility = View.INVISIBLE
    }

    override fun register() {

        val name:String = register_name.text.toString().trim()
        val lastname:String = register_lastname.toString().trim()
        val email:String = register_email.text.toString().trim()
        val password:String = register_password.text.toString().trim()
        val repeatPassword:String = register_repeatpassword.text.toString().trim()

        var checkers = true

        if(presenter.checkEmptyRegisterName(name)){
            register_name.error = "¡Cuidado! El campo 'Nombre' es obligatorio"
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }
        if(presenter.checkEmptyRegisteraLastName(lastname)){
            register_lastname.error = "¡Cuidado! El campo 'Apellidos' es obligatorio"
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }
        if(presenter.checkRegisterEmptyEmail(email)){
            register_email.error = "¡Cuidado! El campo 'Correo Electrónico' es obligatorio"
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }
        if(presenter.checkEmptyRegisterPassword(password)){
            register_password.error = "¡Cuidado! El campo 'Contraseña' es obligatorio"
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }
        if(presenter.checkEmptyRegisterRepeatPassword(repeatPassword)){
            register_repeatpassword.error = "¡Cuidado! El campo 'Repetir Contraseña' es obligatorio"
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }

        if(presenter.checkValidRegisterEmail(email)){
            register_email.error = "¡Cuidado! El Correo Electrónico introducido no es válido."
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }

        if(!presenter.checkRegisterPasswordMatch(password, repeatPassword)){
            register_repeatpassword.error = "¡Cuidado! Las contraseñas no coinciden."
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }

        presenter.registerWithEmailAndPassword(name, lastname, email, password)

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.dettachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dettachView()
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
