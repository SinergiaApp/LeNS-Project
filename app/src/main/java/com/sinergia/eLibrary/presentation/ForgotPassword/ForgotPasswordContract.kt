package com.sinergia.eLibrary.presentation.ForgotPassword

import com.sinergia.eLibrary.presentation.Register.RegisterContract

interface ForgotPasswordContract {

    interface ForgotPasswordView{

        fun showError(error: String)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()

        fun resetPassword()
        fun navigateToMain()
        fun navigateToLogin()

    }

    interface ForgotPasswordPresenter {

        fun attachView(view: RegisterContract.RegisterView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun checkEmptyLoginFields(email: String, password: String, repearPassword: String): Boolean
        fun sendPasswordResetEmail()

    }

}