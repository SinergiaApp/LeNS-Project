package com.sinergia.eLibrary.presentation.Account.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.UseCases.UserUseCases

class AccountViewModelImpl: AccountViewModel, ViewModel() {

    val userUseCases = UserUseCases()

    override suspend fun updateAccount(user: User) {
        return userUseCases.setUser(user)
    }

    override suspend fun deleteAccount(user: User) {
        return userUseCases.deleteUser(user)
    }
}