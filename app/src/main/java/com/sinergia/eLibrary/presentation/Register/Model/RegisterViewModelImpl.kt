package com.sinergia.eLibrary.presentation.Register.Model

import com.sinergia.eLibrary.domain.UseCases.UserUseCases

class RegisterViewModelImpl: RegisterViewModel {

    val userUseCases = UserUseCases()

    override suspend fun addNewUser(
        name: String,
        lastName1: String,
        lastName2: String,
        email: String,
        nif: String,
        loans: MutableList<String>,
        favorites: MutableList<String>,
        admin: Boolean

    ) {

        userUseCases.addUserDB(name, lastName1, lastName2, email, nif, loans, favorites, admin)

    }
}