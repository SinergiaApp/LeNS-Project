package com.sinergia.eLibrary.presentation.Login.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.domain.interactors.LoginInteractor.LoginInteractorImpl
import com.sinergia.eLibrary.presentation.ForgotPassword.View.ForgotPasswordActivity
import com.sinergia.eLibrary.presentation.Login.LoginContract
import com.sinergia.eLibrary.presentation.Login.Presenter.LoginPresenter
import com.sinergia.eLibrary.presentation.Login.Model.LoginViewModel
import com.sinergia.eLibrary.presentation.Login.Model.LoginViewModelImpl
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginContract.LoginView {


    lateinit var loginPresenter: LoginPresenter
    lateinit var loginViewModel: LoginViewModel

    //BASEACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginPresenter = LoginPresenter(LoginInteractorImpl(), LoginViewModelImpl())
        loginPresenter.attachView(this)
        loginViewModel = LoginViewModelImpl()

        login_pass_forgotten.setOnClickListener() { forgotPass() }
        login_btn.setOnClickListener(){ login() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_login
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_LOGIN)
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

    //LOGIN CONTRACT METHODS
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
        login_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        login_progressBar.visibility = View.INVISIBLE
    }

    override fun enableLoginButton() {
        login_btn.isEnabled = true
        login_btn.isClickable = true
    }

    override fun disableLoginButton() {
        login_btn.isEnabled = false
        login_btn.isClickable = false
    }

    override fun forgotPass() {
        var forgotPasswordIntent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        forgotPasswordIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(forgotPasswordIntent)
    }

    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, MainMenuActivity::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }

    override fun login(){

        val email = login_user.text.toString().trim()
        val password = login_pass.text.toString().trim()

        if (loginPresenter.checkEmptyLoginFields(email, password)){

            if(loginPresenter.checkEmptyLoginEmail(email)){
                login_user.error = "¡Cuidado! El campo 'Correo Electrónico' es obligatorio."
            }

            if(loginPresenter.checkEmptyLoginPassword(password)){
                login_pass.error = "¡Cuidado! El campo 'Contraseña' es obligatorio."
            }

            toastL(this, "Vaya... Hay errores en los campos introducidos.")
            return

        } else {
            loginPresenter.logInWithEmailAndPassword(email, password)
        }

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        loginPresenter.dettachView()
        loginPresenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        loginPresenter.dettachView()
        loginPresenter.dettachJob()
    }

}
