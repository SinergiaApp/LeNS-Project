package com.sinergia.eLibrary.presentation.Account.Model

import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.UseCases.UserUseCases

interface AccountViewModel {

    suspend fun deleteUserForUpdate(user: User)
    suspend fun addUserForUpdate(user: User)

    suspend fun deleteAccount(user: User)

}