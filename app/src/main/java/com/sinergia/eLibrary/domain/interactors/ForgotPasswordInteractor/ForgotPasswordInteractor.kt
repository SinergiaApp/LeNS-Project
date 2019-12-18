package com.sinergia.eLibrary.domain.interactors.ForgotPasswordInteractor

interface ForgotPasswordInteractor {

    suspend fun resetPassword(email: String)

}