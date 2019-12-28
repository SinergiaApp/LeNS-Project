package com.sinergia.eLibrary.presentation.Login.Model

import com.sinergia.eLibrary.data.Model.User


interface LoginViewModel {

    suspend fun getCurrentUser(email: String): User

}