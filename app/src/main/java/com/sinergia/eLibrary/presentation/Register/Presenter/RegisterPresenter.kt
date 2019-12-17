package com.sinergia.eLibrary.presentation.Register.Presenter

import android.util.Log
import com.sinergia.eLibrary.domain.interactors.RegisterInteractor.RegisterInteractor
import com.sinergia.eLibrary.presentation.Register.Exceptions.FirebaseRegisterException
import com.sinergia.eLibrary.presentation.Register.RegisterContract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class RegisterPresenter(registerInteractor: RegisterInteractor): RegisterContract.RegisterPresenter, CoroutineScope{

    private val TAG = "[REGISTER_ACTIVITY]"
    private val registerJob = Job()

    var view: RegisterContract.RegisterView? = null
    var registerInteractor: RegisterInteractor? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + registerJob

    init {
        this.registerInteractor = registerInteractor
    }

    override fun attachView(view: RegisterContract.RegisterView) {
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

    override fun checkEmptyFields(name: String, lastName: String, email: String, password: String, repearPassword: String): Boolean {
        return name.isNullOrEmpty() || lastName.isNullOrEmpty() || email.isNullOrEmpty() || password.isNullOrEmpty() || repearPassword.isNullOrEmpty()
    }


    override fun checkEmptyRegisterName(name: String): Boolean {
        return name.isNullOrEmpty()
    }

    override fun checkEmptyRegisteraLastName(lastName: String): Boolean {
        return lastName.isNullOrEmpty()
    }

    override fun checkRegisterEmptyEmail(email: String): Boolean {
        return email.isNullOrEmpty()
    }

    override fun checkEmptyRegisterPassword(password: String): Boolean {
        return password.isNullOrEmpty()
    }

    override fun checkEmptyRegisterRepeatPassword(repearPassword: String): Boolean {
        return repearPassword.isNullOrEmpty()
    }

    override fun checkValidRegisterEmail(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun checkRegisterPasswordMatch(password: String, repearPassword: String): Boolean {
        return password == repearPassword
    }

    override fun registerWithEmailAndPassword(name: String, lastName: String, email: String, password: String) {

        launch {

            Log.d(TAG, "Trying to register with email $email...")

            view?.showProgressBar()
            view?.disableRegisterButton()

            try {
                registerInteractor?.register(name, lastName, email, password)

                if(isViewAttach()){
                    view?.navigateToMainPage()
                    view?.hideProgressBar()
                    view?.enableRegisterButton()

                    Log.d(TAG, "Sucesfully register with email $email.")
                }
            }catch(error: FirebaseRegisterException){

                val errorMsg = error?.message
                view?.showError(errorMsg)
                view?.hideProgressBar()
                view?.enableRegisterButton()

                Log.d(TAG, "ERROR: Cannot register with email $email --> $errorMsg")

            }

        }
    }
}