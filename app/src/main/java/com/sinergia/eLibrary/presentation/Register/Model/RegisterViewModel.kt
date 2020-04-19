package com.sinergia.eLibrary.presentation.Register.Model

import com.sinergia.eLibrary.data.Model.User

interface RegisterViewModel {

    suspend fun addNewUser(newUser: User)

}