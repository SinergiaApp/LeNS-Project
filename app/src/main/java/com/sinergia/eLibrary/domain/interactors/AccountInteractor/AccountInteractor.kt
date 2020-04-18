package com.sinergia.eLibrary.domain.interactors.AccountInteractor

import com.sinergia.eLibrary.data.Model.User

interface AccountInteractor {

    suspend fun deleteUser(user: User)

    suspend fun logOut()

}