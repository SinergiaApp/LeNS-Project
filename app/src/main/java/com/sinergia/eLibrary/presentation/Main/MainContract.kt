package com.sinergia.eLibrary.presentation.Main

interface MainContract {

    interface MainView{

        fun showError(error: String)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()

        fun navToLoginPage()
        fun navToRegisterPage()
        fun googleLogin()

    }

}