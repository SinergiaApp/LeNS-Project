package com.sinergia.eLibrary.domain.interactors.LoginInteractor

interface LoginInteractor {

    suspend fun LoginWithEmailAndPassword(email: String, password: String)

}