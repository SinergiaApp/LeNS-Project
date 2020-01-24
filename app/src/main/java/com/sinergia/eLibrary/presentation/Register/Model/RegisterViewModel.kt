package com.sinergia.eLibrary.presentation.Register.Model

interface RegisterViewModel {

    suspend fun addNewUser(
        name: String,
        lastName1: String,
        lastName2: String,
        email: String,
        nif: String,
        loans: MutableList<String>,
        favorites: MutableList<String>,
        admin: Boolean
    )

}