package com.sinergia.eLibrary.presentation.Register.Model

interface RegisterViewModel {

    suspend fun addNewUser(name: String, lastname: String, email: String, admin: Boolean, resources: Map<String, String>)

}