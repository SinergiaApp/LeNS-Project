package com.sinergia.eLibrary.presentation.ForgotPassword.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.domain.interactors.ForgotPasswordInteractor.ForgotPasswordInteractorImpl
import com.sinergia.eLibrary.presentation.ForgotPassword.ForgotPasswordContract
import com.sinergia.eLibrary.presentation.ForgotPassword.Presenter.ForgotPasswordPresenter
import com.sinergia.eLibrary.presentation.Login.View.LoginActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : BaseActivity(), ForgotPasswordContract.ForgotPasswordView {

    //BASE ACTIVITY METHODS
    lateinit var forgotPasswordPresenter: ForgotPasswordPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        forgotPasswordPresenter = ForgotPasswordPresenter(ForgotPasswordInteractorImpl())
        forgotPasswordPresenter.attachView(this)

        disableGoToLoginButton()
        forgot_pass_btn.setOnClickListener{ resetPassword() }
        forgot_pass_goToLogin_btn.setOnClickListener { navigateToLogin() }

    }
    override fun getLayout(): Int {
        return R.layout.activity_forgot_password
    }

    override fun getPageTitle(): String {
        return "Reseteo de Contraseña"
    }

    //FORGOT PASSWORD CONTRACT METHODS
    override fun showError(error: String?) {
        toastS(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showProgressBar() {
        forgot_pass_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        forgot_pass_progressBar.visibility = View.INVISIBLE
    }

    override fun enableResetPasswordButton() {
        forgot_pass_btn.isEnabled = true
        forgot_pass_btn.isClickable = true
    }

    override fun disableResetPasswordButton() {
        forgot_pass_btn.isEnabled = false
        forgot_pass_btn.isClickable = false
    }

    override fun enableGoToLoginButton() {
        forgot_pass_goToLogin_btn.isEnabled = true
        forgot_pass_goToLogin_btn.isClickable = true
    }

    override fun disableGoToLoginButton() {
        forgot_pass_goToLogin_btn.isEnabled = false
        forgot_pass_goToLogin_btn.isClickable = false
    }

    override fun resetPassword() {

        val email = forgot_pass_email.text.toString()

        if(forgotPasswordPresenter.checkResetPasswordEmptyEmail(email)){
            forgot_pass_email.error = "¡Cuidado! El campo 'Correo Electrónico' es obligatorio."
            toastL(this, "Vaya... Hay errores en los campos introducidos.")
        } else if(forgotPasswordPresenter.checkResetPasswordValidEmail(email)) {
            forgot_pass_email.error = "¡Cuidado! El formato del campo 'Correo Electrónico' es incorrecto."
            toastL(this, "Vaya... Hay errores en los campos introducidos.")
        } else {
            forgotPasswordPresenter.sendPasswordResetEmail(email)
        }

    }

    override fun navigateToLogin() {

        val intentLoginPage = Intent(this, LoginActivity::class.java)
        intentLoginPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentLoginPage)

    }

    override fun onDetachedFromWindow(){
        super.onDetachedFromWindow()
        forgotPasswordPresenter.dettachView()
        forgotPasswordPresenter.detachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        forgotPasswordPresenter.dettachView()
        forgotPasswordPresenter.detachJob()
    }
}
