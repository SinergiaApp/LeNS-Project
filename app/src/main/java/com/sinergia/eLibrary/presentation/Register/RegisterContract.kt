package com.sinergia.eLibrary.presentation.Register

import com.sinergia.eLibrary.presentation.Register.RegisterContract

interface RegisterContract {

    interface RegisterView {

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun enableRegisterButton()
        fun disableRegisterButton()
        fun register()
        fun navigateToMainPage()
        fun navigateToRegister()

    }

    interface RegisterPresenter {

        fun attachView(view: RegisterView)
        fun dettachView()
        fun dettachJob()
        fun isViewAttach(): Boolean
        fun registerWithEmailAndPassword(name:String, lastName: String, email: String, password: String)

        fun checkEmptyFields(name: String, lastName:String, email: String, password: String, repearPassword: String): Boolean
        fun checkEmptyRegisterName(name: String): Boolean
        fun checkEmptyRegisteraLastName(lastName: String): Boolean
        fun checkRegisterEmptyEmail(email: String): Boolean
        fun checkEmptyRegisterPassword(password: String): Boolean
        fun checkEmptyRegisterRepeatPassword(repearPassword: String): Boolean

        fun checkValidRegisterEmail(email: String): Boolean
        fun checkRegisterPasswordMatch(password: String, repearPassword: String): Boolean

    }
}