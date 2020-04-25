package com.sinergia.eLibrary.presentation.Account.Model

import android.net.Uri
import com.sinergia.eLibrary.data.Model.User

interface AccountViewModel {

    suspend fun deleteUserForUpdate(user: User)
    suspend fun addUserForUpdate(user: User)
    suspend fun setUserForUpdate(settedUser: User)

    suspend fun deleteAccount(user: User)

    suspend fun uploadImage(owner: String, imageURI: Uri): Uri
}