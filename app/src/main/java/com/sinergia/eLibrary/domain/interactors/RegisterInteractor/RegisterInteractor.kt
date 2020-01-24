package com.sinergia.eLibrary.domain.interactors.RegisterInteractor

interface RegisterInteractor {

    suspend fun register(name: String, email: String, password: String)

}