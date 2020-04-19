package com.sinergia.eLibrary.presentation.Register.Model

import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.UseCases.UserUseCases

class RegisterViewModelImpl: RegisterViewModel {

    val userUseCases = UserUseCases()

    override suspend fun addNewUser(newUser: User) {

        userUseCases.addUser(newUser)

    }
}