package com.sinergia.eLibrary.presentation.Main.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
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
        setContentView(R.layout.activity_main)

        main_login_btn.setOnClickListener { navToLoginPage() }
        main_register_btn.setOnClickListener { navToRegisterPage() }

    }

    //MAINCONTRACT METHODS
    override fun showError(error: String) {
        toast(this, error, "s")
    }

    override fun showMessage(message: String) {
        toast(this, message, "s")
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
