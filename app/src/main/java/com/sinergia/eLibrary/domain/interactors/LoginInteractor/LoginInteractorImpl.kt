package com.sinergia.eLibrary.domain.interactors.LoginInteractor

import com.google.firebase.auth.FirebaseAuth

class LoginInteractorImpl: LoginInteractor {

    override fun LoginWithEmailAndPassword(email: String, password: String, listener: LoginInteractor.LoginCallback) {

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if(it.isSuccessful){
                    listener.onLoginSuccess()
                } else {
                    listener.onLoginFailure(it.exception?.message!!)
                }
            }

    }

}