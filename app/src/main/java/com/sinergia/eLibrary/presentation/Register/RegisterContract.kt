package com.sinergia.eLibrary.presentation.Register

interface RegisterContract {

    interface RegisterView {

        fun showError(error: String)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun register()
        fun navigateToLogin()

    }

    interface RegisterPresenter {

        fun attachView(view: RegisterView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun registerWithEmailAndPassword(email: String, password: String)
        fun checkEmptyLoginFields(email: String, password: String, repearPassword: String): Boolean

    }
}