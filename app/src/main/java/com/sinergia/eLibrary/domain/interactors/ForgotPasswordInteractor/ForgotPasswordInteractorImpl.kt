package com.sinergia.eLibrary.domain.interactors.ForgotPasswordInteractor

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.sinergia.eLibrary.base.Exceptions.FirebaseResetPasswordException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class ForgotPasswordInteractorImpl: ForgotPasswordInteractor {

    @ExperimentalCoroutinesApi
    override suspend fun resetPassword(email: String): Unit = suspendCancellableCoroutine { resetPasswordContinuation ->

        val TAG = "[FORGOT_PASS_ACTIVITY]"

        FirebaseAuth
            .getInstance()
            .sendPasswordResetEmail(email)
            .addOnCompleteListener { resetPassword ->
                if(resetPassword.isSuccessful){
                    resetPasswordContinuation.resume(Unit)
                    Log.d(TAG, "successfully send an email to reset password to account with email $email.")
                } else {
                    val errorMsg = resetPassword.exception?.message.toString()
                    Log.d(TAG, "ERROR: Cannot sent email to reset password to account with email $email --> $errorMsg")
                    resetPasswordContinuation.resumeWithException(
                        FirebaseResetPasswordException(
                            errorMsg
                        )
                    )
                }
            }

    }


}