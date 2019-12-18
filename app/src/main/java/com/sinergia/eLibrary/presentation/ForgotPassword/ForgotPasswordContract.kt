package com.sinergia.eLibrary.presentation.ForgotPassword

import com.sinergia.eLibrary.presentation.Register.RegisterContract

interface ForgotPasswordContract {

    interface ForgotPasswordView{

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()

        fun enableGoToLoginButton()
        fun disableGoToLoginButton()
        fun enableResetPasswordButton()
        fun disableResetPasswordButton()


        fun resetPassword()
        fun navigateToLogin()

    }

    interface ForgotPasswordPresenter {

        fun attachView(view: ForgotPasswordContract.ForgotPasswordView)
        fun dettachView()
        fun detachJob()
        fun isViewAttach(): Boolean
        fun checkResetPasswordEmptyEmail(email: String): Boolean
        fun checkResetPasswordValidEmail(email: String): Boolean
        fun sendPasswordResetEmail(email: String)

    }

}