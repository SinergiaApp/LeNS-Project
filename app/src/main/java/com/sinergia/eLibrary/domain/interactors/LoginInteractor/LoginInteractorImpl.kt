package com.sinergia.eLibrary.domain.interactors.LoginInteractor

import com.google.firebase.auth.FirebaseAuth
import com.sinergia.eLibrary.presentation.Login.Exceptions.FirebaseLoginException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class LoginInteractorImpl: LoginInteractor {

    @ExperimentalCoroutinesApi
    override suspend fun LoginWithEmailAndPassword(email: String, password: String): Unit = suspendCancellableCoroutine { loginContinuation ->

        FirebaseAuth
            .getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { loginTask ->
                if(loginTask.isSuccessful){
                    loginContinuation.resume(Unit)
                } else {
                    loginContinuation.resumeWithException(FirebaseLoginException(loginTask.exception?.message.toString()))
                }
            }

    }

}