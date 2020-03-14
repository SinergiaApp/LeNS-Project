package com.sinergia.eLibrary.presentation.Login.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.UseCases.UserUseCases

class LoginViewModelImpl: ViewModel(), LoginViewModel {

    val userUseCase = UserUseCases()

    override suspend fun getCurrentUser(email:String): User {
        return userUseCase.getUser(email)
    }
}