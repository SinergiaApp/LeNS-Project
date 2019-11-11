package com.sinergia.eLibrary.domain.interactors.LoginInteractor

interface LoginInteractor {

    interface LoginCallback {

        fun onLoginSuccess()
        fun onLoginFailure(errorMsg: String)

    }

    fun LoginWithEmailAndPassword(email: String, password: String, listener:LoginCallback)


}