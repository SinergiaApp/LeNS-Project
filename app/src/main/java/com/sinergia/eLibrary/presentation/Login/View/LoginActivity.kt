package com.sinergia.eLibrary.presentation.Login.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.ForgotPassword.View.ForgotPasswordActivity
import com.sinergia.eLibrary.presentation.Login.LoginContract
import com.sinergia.eLibrary.presentation.Login.Presenter.LoginPresenter
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(), LoginContract.LoginView {

    //BASEACTIVITY METHODS
    lateinit var presenter: LoginPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.attachView(this)
        setContentView(R.layout.activity_login)

        login_pass_forgotten.setOnClickListener() { forgotPass() }
        main_login_btn.setOnClickListener(){ login() }

    }


    //LOGIN CONTRACT METHODS
    override fun showError(error: String) {
        toast(this, error, "s")
    }

    override fun showMessage(message: String) {
        toast(this, message, "s")
    }

    override fun showProgressBar() {
        login_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        login_progressBar.visibility = View.INVISIBLE
    }

    override fun forgotPass() {
        var forgotPasswordIntent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
        startActivity(forgotPasswordIntent)
    }

    override fun navigateToMainPage() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun login(){

        val email = login_user.text.toString().trim()
        val password = login_pass.text.toString().trim()

        if (presenter.checkEmptyLoginFields(email, password)){
            showError("Todos los campos son oblicatorios, por favor completa el formulario completo.")
        } else {
            presenter.logInWithEmailAndPassword(email, password)
        }

    }

}
