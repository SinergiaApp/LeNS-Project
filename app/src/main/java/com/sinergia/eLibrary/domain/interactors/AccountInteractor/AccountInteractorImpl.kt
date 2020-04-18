package com.sinergia.eLibrary.domain.interactors.AccountInteractor

import com.google.firebase.auth.FirebaseAuth
import com.sinergia.eLibrary.base.Exceptions.FirebaseDeleteUserException
import com.sinergia.eLibrary.data.Model.User
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class AccountInteractorImpl: AccountInteractor {

    override suspend fun deleteUser(user: User): Unit = suspendCancellableCoroutine {deleteUserContinuation ->

        FirebaseAuth
            .getInstance()
            .currentUser!!
            .delete()
            .addOnCompleteListener { deleteUser ->

                if(deleteUser.isSuccessful){

                    deleteUserContinuation.resume(Unit)

                } else {

                    deleteUserContinuation.resumeWithException(
                        FirebaseDeleteUserException(deleteUser.exception?.message.toString())
                    )

                }

            }

    }

    override suspend fun logOut() {
        FirebaseAuth
            .getInstance()
            .signOut()
    }
}