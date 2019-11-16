package com.sinergia.eLibrary.presentation.Main.View

import android.content.Intent
import android.os.Bundle
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Login.View.LoginActivity
import com.sinergia.eLibrary.presentation.Main.MainContract
import com.sinergia.eLibrary.presentation.Register.View.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.MainView {


    //BASEACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        main_login_btn.setOnClickListener { navToLoginPage() }
        main_register_btn.setOnClickListener { navToRegisterPage() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    //MAIN CONTRACT METHODS
    override fun showError(error: String) {
        toastS(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showProgressBar() {
        TODO("Need to add a ProgressBar to the Layout Activity")
    }

    override fun hideProgressBar() {
        TODO("Need to add a ProgressBar to the Layout Activity")
    }

    override fun navToLoginPage() {
        startActivity(Intent(this, LoginActivity::class.java))
    }

    override fun navToRegisterPage() {
        startActivity(Intent(this, RegisterActivity::class.java))
    }

}
