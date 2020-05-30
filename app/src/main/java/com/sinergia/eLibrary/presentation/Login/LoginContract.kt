package com.sinergia.eLibrary.presentation.Login


interface LoginContract {

    interface LoginView{

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showError(error: Int)
        fun showMessage(message: Int)
        fun showProgressBar()
        fun hideProgressBar()
        fun enableLoginButton()
        fun disableLoginButton()
        fun login()
        fun forgotPass()
        fun navigateToMainPage()

    }

    interface LoginPresenter {
        fun attachView(view: LoginView)
        fun dettachView()
        fun dettachJob()
        fun isViewAttach(): Boolean
        fun logInWithEmailAndPassword(email: String, password: String)
        fun checkEmptyLoginFields(email: String, password: String): Boolean
        fun checkEmptyLoginEmail(email: String): Boolean
        fun checkEmptyLoginPassword(password: String): Boolean

    }


}