package com.sinergia.eLibrary.presentation.Register.View

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.interactors.RegisterInteractor.RegisterInteractorImpl
import com.sinergia.eLibrary.presentation.Catalog.View.CatalogActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Register.Model.RegisterViewModelImpl
import com.sinergia.eLibrary.presentation.Register.Presenter.RegisterPresenter
import com.sinergia.eLibrary.presentation.Register.RegisterContract
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : BaseActivity(), RegisterContract.RegisterView {
    
    //PRESENTER INITIALIZATION
    lateinit var registerPresenter: RegisterPresenter

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        registerPresenter = RegisterPresenter(RegisterInteractorImpl(), RegisterViewModelImpl())
        registerPresenter.attachView(this)

        register_btn.setOnClickListener { register() }
    }

    override fun getLayout(): Int {
        return R.layout.activity_register
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_REGISTER)
    }

    override fun backButton() {
        if(NeLSProject.backButtonPressedTwice){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("EXIT", true)
            startActivity(intent)
        } else {
            toastL(this, getString(R.string.BTN_BACK))
            NeLSProject.backButtonPressedTwice = true
        }
    }

    //REGISTER CONTRACT METHODS
    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, MainMenuActivity::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }

    override fun navigateToRegister() {
        val intentMainPage = Intent(this, RegisterActivity::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }

    override fun showError(error: String?) {
        toastS(this, error)
    }

    override fun showError(error: Int) {
        toastL(this, getString(error))
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showMessage(message: Int) {
        toastL(this, getString(message))
    }

    override fun showProgressBar() {
        register_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        register_progressBar.visibility = View.INVISIBLE
    }

    override fun enableRegisterButton() {
        register_btn.isEnabled = true
        register_btn.isClickable = true
    }

    override fun disableRegisterButton() {
        register_btn.isEnabled = false
        register_btn.isClickable = false
    }

    override fun register() {

        val name:String = register_name.text.toString().trim()
        val lastNames:String = register_lastname.text.toString()

        var lastName1: String
        var lastName2: String
        if(lastNames.contains(" ")){
            lastName1 = lastNames.substring(0, lastNames.indexOf(" "))
            lastName2 = lastNames.substring(lastNames.indexOf(" ")+1)
        } else {
            lastName1 = lastNames
            lastName2 = ""
        }

        val email:String = register_email.text.toString().trim()
        val nif:String = register_nif.text.toString().trim()
        val password:String = register_password.text.toString().trim()
        val repeatPassword:String = register_repeatpassword.text.toString().trim()

        if(registerPresenter.checkEmptyFields(name, lastName1, lastName2, email, nif, password, repeatPassword)) {

            if (registerPresenter.checkEmptyRegisterName(name)) {
                register_name.error = "¡Cuidado! El campo 'Nombre' es obligatorio."
            }
            if (registerPresenter.checkEmptyRegisterLastName1(lastName1)) {
                register_lastname.error = "¡Cuidado! El campo 'Apellidos' es obligatorio."
            }
            if (registerPresenter.checkEmptyRegisterLastName2(lastName2)) {
                register_lastname.error = "¡Cuidado! El campo 'Apellidos' es obligatorio."
            }
            if (registerPresenter.checkRegisterEmptyEmail(email)) {
                register_email.error = "¡Cuidado! El campo 'Correo Electrónico' es obligatorio."
            }
            if (registerPresenter.checkEmptyRegisterNIF(nif)) {
                register_nif.error = "¡Cuidado! El campo 'NIF' es obligatorio."
            }
            if (registerPresenter.checkEmptyRegisterPassword(password)) {
                register_password.error = "¡Cuidado! El campo 'Contraseña' es obligatorio"
            }
            if (registerPresenter.checkEmptyRegisterRepeatPassword(repeatPassword)) {
                register_repeatpassword.error = "¡Cuidado! El campo 'Repetir Contraseña' es obligatorio."
            }

            toastL(this, "Vaya... Hay errores en los campos introducidos.")
            return

        } else {

            if(!registerPresenter.checkValidRegisterEmail(email)){
                register_email.error = "¡Cuidado! El Correo Electrónico introducido no es válido."
                toastS(this, "Vaya... Hay errores en los campos introducidos.")
                return
            }

            if(!registerPresenter.checkRegisterPasswordMatch(password, repeatPassword)){
                register_repeatpassword.error = "¡Cuidado! Las contraseñas no coinciden."
                toastS(this, "Vaya... Hay errores en los campos introducidos.")
                return
            }

            var newUser = User(
                name,
                lastName1,
                lastName2,
                email,
                nif
            )
            registerPresenter.registerWithEmailAndPassword(newUser, password)

        }


    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        registerPresenter.dettachView()
        registerPresenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        registerPresenter.dettachView()
        registerPresenter.dettachJob()
    }

}
