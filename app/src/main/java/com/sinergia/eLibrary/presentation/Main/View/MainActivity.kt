package com.sinergia.eLibrary.presentation.Main.View

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Login.View.LoginActivity
import com.sinergia.eLibrary.presentation.Main.MainContract
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Register.View.RegisterActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), MainContract.MainView {

    //BASEACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish()
        }

        main_login_btn.setOnClickListener { navToLoginPage() }
        main_register_btn.setOnClickListener { navToRegisterPage() }
        main_login_google_btn.setOnClickListener { googleLogin() }

        NeLSProject.storagePermissionGranted = (
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)

        NeLSProject.cameraPermissionGranted = (
                ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)

    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_MAIN)
    }

    override fun backButton() {
        if(NeLSProject.backButtonPressedTwice){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            intent.putExtra("EXIT", true)
            startActivity(intent)
        } else {
            toastL(this, getString(R.string.BTN_BACK))
            NeLSProject.backButtonPressedTwice = true
        }
    }


    //MAIN CONTRACT METHODS
    override fun showError(error: String) {
        toastS(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showProgressBar() {
        main_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        main_progressBar.visibility = View.INVISIBLE
    }

    override fun enableButtons() {
        main_login_btn.isClickable = true
        main_login_btn.isEnabled = true
        main_register_btn.isClickable = true
        main_register_btn.isEnabled = true
    }

    override fun disableButtons() {
        main_login_btn.isClickable = false
        main_login_btn.isEnabled = false
        main_register_btn.isClickable = false
        main_register_btn.isEnabled = false
    }

    override fun navToLoginPage() {
        disableButtons()
        showProgressBar()
        val loginIntent = Intent(this, LoginActivity::class.java)
        loginIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(loginIntent)
    }

    override fun navToRegisterPage() {
        disableButtons()
        showProgressBar()
        val registerIntent = Intent(this, RegisterActivity::class.java)
        registerIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(registerIntent)
    }

    override fun googleLogin() {
        toastL(this, "Esto aun no va!! =(, no le des mas al botón pesadilla !!")
    }

}
