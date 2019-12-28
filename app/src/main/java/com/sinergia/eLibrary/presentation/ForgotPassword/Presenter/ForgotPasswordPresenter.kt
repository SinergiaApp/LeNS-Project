package com.sinergia.eLibrary.presentation.ForgotPassword.Presenter

import android.util.Log
import com.sinergia.eLibrary.domain.interactors.ForgotPasswordInteractor.ForgotPasswordInteractor
import com.sinergia.eLibrary.base.Exceptions.FirebaseResetPasswordException
import com.sinergia.eLibrary.presentation.ForgotPassword.ForgotPasswordContract
import com.sinergia.eLibrary.presentation.ForgotPassword.ForgotPasswordContract.ForgotPasswordView
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ForgotPasswordPresenter(forgotPasswordInteractor: ForgotPasswordInteractor): ForgotPasswordContract.ForgotPasswordPresenter, CoroutineScope  {

    private val TAG = "[RESET_PASS_ACTIVITY]"
    private val resertPasswordJob = Job()

    var view: ForgotPasswordView ?= null
    var forgotPasswordInteractor: ForgotPasswordInteractor ?= null


    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + resertPasswordJob

    init {
        this.forgotPasswordInteractor = forgotPasswordInteractor
    }

    override fun attachView(view: ForgotPasswordView) {
        this.view = view
    }

    override fun dettachView() {
        this.view = null
    }

    override fun detachJob() {
        coroutineContext.cancel()
    }

    override fun isViewAttach(): Boolean {
        return this.view != null
    }

    override fun checkResetPasswordEmptyEmail(email: String): Boolean {
        return email.isNullOrEmpty()
    }

    override fun checkResetPasswordValidEmail(email: String): Boolean {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun sendPasswordResetEmail(email: String) {

        launch {

            view?.showProgressBar()
            view?.disableGoToLoginButton()
            view?.disableResetPasswordButton()

            try{

                forgotPasswordInteractor?.resetPassword(email)

                view?.hideProgressBar()
                view?.enableGoToLoginButton()
                view?.showMessage("Se ha enviado el email de reseteo de contraseña al correo $email.")

                Log.d(TAG, "Sucesfully send email to reset password to account with email $email.")

            } catch (error: FirebaseResetPasswordException){

                val errorMsg = error?.message
                view?.hideProgressBar()
                view?.showError(errorMsg)
                view?.enableResetPasswordButton()

                Log.d(TAG, "ERROR: Cannot send email to reset password to account with email $email --> $errorMsg")

            }

        }

    }
}