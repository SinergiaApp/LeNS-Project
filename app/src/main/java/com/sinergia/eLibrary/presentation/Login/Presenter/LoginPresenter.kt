package com.sinergia.eLibrary.presentation.Login.Presenter

import com.sinergia.eLibrary.domain.interactors.LoginInteractor.LoginInteractor
import com.sinergia.eLibrary.presentation.Login.LoginContract

class LoginPresenter(LoginInteractor: LoginInteractor): LoginContract.LoginPresenter {


    var view: LoginContract.LoginView? = null
    var loginInteractor: LoginInteractor? = null

    init {
        this.loginInteractor = LoginInteractor
    }


    override fun attachView(view: LoginContract.LoginView) {
        this.view = view
    }

    override fun dettachView() {
        view = null
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun logInWithEmailAndPassword(email: String, password: String) {
        view?.showProgressBar()
        loginInteractor!!.LoginWithEmailAndPassword(email, password, object:LoginInteractor.LoginCallback{

            override fun onLoginSuccess() {
                if(isViewAttach()){
                    view?.hideProgressBar()
                    view?.navigateToMainPage()
                }
            }

            override fun onLoginFailure(errorMsg: String) {
                if(isViewAttach()){
                    view?.hideProgressBar()
                    view?.showError(errorMsg)
                }

            }

        } )

    }

    override fun checkEmptyLoginFields(email: String, password: String): Boolean {
        return (email.isEmpty() || password.isEmpty())
    }

}