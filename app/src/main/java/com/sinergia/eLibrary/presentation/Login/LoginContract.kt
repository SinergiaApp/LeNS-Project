package com.sinergia.eLibrary.presentation.Login


interface LoginContract {

    interface LoginView{
        fun showError(error: String)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun login()
        fun googleLogin()
        fun forgotPass()
        fun navigateToMainPage()

    }

    interface LoginPresenter {
        fun attachView(view: LoginView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun logInWithEmailAndPassword(email: String, password: String)
        fun checkEmptyLoginFields(email: String, password: String): Boolean

    }


}