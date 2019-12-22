package com.sinergia.eLibrary.presentation.Register.Model

import com.sinergia.eLibrary.domain.UseCases.UserUseCase

class RegisterViewModelImpl: RegisterViewModel {

    val userUseCases = UserUseCase()

    override suspend fun addNewUser(name: String, lastname: String, email: String, admin: Boolean, resources: Map<String, String>) {

        userUseCases.addUserDB(name, lastname, email, admin, resources)

    }
}