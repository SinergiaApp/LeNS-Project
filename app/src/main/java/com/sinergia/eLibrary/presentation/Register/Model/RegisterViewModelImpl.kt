package com.sinergia.eLibrary.presentation.Register.Model

import com.sinergia.eLibrary.domain.UseCases.UserUseCase

class RegisterViewModelImpl: RegisterViewModel {

    val userUseCases = UserUseCase()

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