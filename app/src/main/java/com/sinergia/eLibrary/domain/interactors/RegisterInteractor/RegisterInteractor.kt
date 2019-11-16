package com.sinergia.eLibrary.domain.interactors.RegisterInteractor

interface RegisterInteractor {

    interface RegisterCallBack{
        fun onRegisterSuccess()
        fun onRegisterFailure(erroMsg: String)
    }

    fun register(name: String, lastName: String, email: String, password: String, listener: RegisterCallBack)

}