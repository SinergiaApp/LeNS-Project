package com.sinergia.eLibrary.presentation.Register.View

//import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.domain.interactors.RegisterInteractor.RegisterInteractorImpl
import com.sinergia.eLibrary.presentation.Catalog.View.CatalogActivity
import com.sinergia.eLibrary.presentation.Register.Presenter.RegisterPresenter
import com.sinergia.eLibrary.presentation.Register.RegisterContract
import kotlinx.android.synthetic.main.activity_register.*

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
        return R.layout.activity_register
    }

    override fun getPageTitle(): String {
        return "REGISTRO"
    }

    //REGISTER CONTRACT METHODS
    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, CatalogActivity::class.java)
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

    override fun showMessage(message: String) {
        toastS(this, message)
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

        showProgressBar()

        val name:String = register_name.text.toString().trim()
        val lastName:String = register_lastname.toString().trim()
        val email:String = register_email.text.toString().trim()
        val password:String = register_password.text.toString().trim()
        val repeatPassword:String = register_repeatpassword.text.toString().trim()

        if(presenter.checkEmptyFields(name, lastName, email, password, repeatPassword)) {

            if (presenter.checkEmptyRegisterName(name)) {
                register_name.error = "¡Cuidado! El campo 'Nombre' es obligatorio."
            }
            if (presenter.checkEmptyRegisteraLastName(lastName)) {
                register_lastname.error = "¡Cuidado! El campo 'Apellidos' es obligatorio."
            }
            if (presenter.checkRegisterEmptyEmail(email)) {
                register_email.error = "¡Cuidado! El campo 'Correo Electrónico' es obligatorio."
            }
            if (presenter.checkEmptyRegisterPassword(password)) {
                register_password.error = "¡Cuidado! El campo 'Contraseña' es obligatorio"
            }
            if (presenter.checkEmptyRegisterRepeatPassword(repeatPassword)) {
                register_repeatpassword.error =
                    "¡Cuidado! El campo 'Repetir Contraseña' es obligatorio."
            }

            toastL(this, "Vaya... Hay errores en los campos introducidos.")
            return

        } else {

            if(!presenter.checkValidRegisterEmail(email)){
                register_email.error = "¡Cuidado! El Correo Electrónico introducido no es válido."
                toastS(this, "Vaya... Hay errores en los campos introducidos.")
                return
            }

            if(!presenter.checkRegisterPasswordMatch(password, repeatPassword)){
                register_repeatpassword.error = "¡Cuidado! Las contraseñas no coinciden."
                toastS(this, "Vaya... Hay errores en los campos introducidos.")
                return
            }

            presenter.registerWithEmailAndPassword(name, lastName, email, password)

        }


    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.dettachView()
        presenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.dettachView()
        presenter.dettachJob()
    }

}
