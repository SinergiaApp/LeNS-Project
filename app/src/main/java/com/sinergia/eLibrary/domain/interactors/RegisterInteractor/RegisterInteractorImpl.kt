package com.sinergia.eLibrary.domain.interactors.RegisterInteractor

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.sinergia.eLibrary.domain.UseCases.UserUseCase
import com.sinergia.eLibrary.presentation.Register.Exceptions.FirebaseRegisterException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.resumeCancellableWith
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class RegisterInteractorImpl: RegisterInteractor {

    @UseExperimental(ExperimentalCoroutinesApi::class)
    override suspend fun register(name: String, lastName: String, email: String, password: String):Unit = suspendCancellableCoroutine  { registerContinuation ->

        val TAG = "[REGISTER_ACTIVITY]"

        FirebaseAuth
            .getInstance()
            .createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{ registerTask ->
                if(registerTask.isSuccessful){

                    var profileUpdates = UserProfileChangeRequest
                        .Builder()
                        .setDisplayName(name)
                        .build()

                    FirebaseAuth.getInstance().currentUser?.updateProfile(profileUpdates)!!
                        .addOnCompleteListener{updateProfileTask ->
                            if(updateProfileTask.isSuccessful) {
                                registerContinuation.resume(Unit)
                                Log.d(TAG, "successfully update user profile after register with email $email.")
                            } else {
                                val errorMsg = registerTask.exception?.message.toString()
                                Log.d(TAG, "ERROR: Cannot update user profile after register with email $email --> $errorMsg")
                                registerContinuation.resumeWithException(FirebaseRegisterException(errorMsg))
                            }
                        }

                } else {
                    registerContinuation.resumeWithException(FirebaseRegisterException(registerTask.exception?.message.toString()))
                }
            }

    }

}