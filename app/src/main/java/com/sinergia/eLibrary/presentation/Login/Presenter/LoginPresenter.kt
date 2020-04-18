package com.sinergia.eLibrary.presentation.Login.Presenter

import android.util.Log
import com.sinergia.eLibrary.domain.interactors.LoginInteractor.LoginInteractor
import com.sinergia.eLibrary.base.Exceptions.FirebaseLoginException
import com.sinergia.eLibrary.presentation.Login.LoginContract
import com.sinergia.eLibrary.presentation.Login.Model.LoginViewModelImpl
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetUserException
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext
import com.sinergia.eLibrary.presentation.NeLSProject.Companion as NeLSVars

class LoginPresenter(loginInteractor: LoginInteractor, loginViewModel: LoginViewModelImpl): LoginContract.LoginPresenter, CoroutineScope {

    val TAG = "[LOGIN_ACTIVITY]"

    var view: LoginContract.LoginView ?= null
    var loginInteractor: LoginInteractor ?= null
    var loginViewModel: LoginViewModelImpl ?= null
    private val loginJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + loginJob

    init {
        this.loginInteractor = loginInteractor
        this.loginViewModel = loginViewModel
    }


    override fun attachView(view: LoginContract.LoginView) {
        this.view = view
    }

    override fun dettachView() {
        view = null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun checkEmptyLoginFields(email: String, password: String): Boolean {
        return (email.isEmpty() || password.isEmpty())
    }

    override fun checkEmptyLoginEmail(email: String): Boolean {
        return email.isNullOrEmpty()
    }

    override fun checkEmptyLoginPassword(password: String): Boolean {
        return password.isNullOrEmpty()
    }

    override fun logInWithEmailAndPassword(email: String, password: String) {

            launch {

            Log.d(TAG, "Trying to login with email $email...")

            view?.showProgressBar()
            view?.disableLoginButton()

            try {
                loginInteractor?.LoginWithEmailAndPassword(email, password)

                try{
                    val currentUser = loginViewModel?.getCurrentUser(email)
                    NeLSVars.adminUser = currentUser!!.admin
                    NeLSVars.currentUser = currentUser
                }catch (error: FirebaseGetUserException){

                    val errorMsg = error.message
                    if(isViewAttach()) view?.showError("No se ha podido recuperar tu usuario de la base de datos.")

                    Log.d(TAG, "ERROR: Cannot get user with email $email --> $errorMsg")
                }


                if(isViewAttach()){
                    view?.hideProgressBar()
                    view?.enableLoginButton()
                    view?.navigateToMainPage()

                    Log.d(TAG, "Succesfully logged User -> ${NeLSVars.currentUser.toString()}.")
                }
            }catch (error: FirebaseLoginException) {

                val errorMsg = error.message
                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableLoginButton()
                }

                Log.d(TAG, "ERROR: Cannot login with email $email --> $errorMsg")
            }


        }

    }

}