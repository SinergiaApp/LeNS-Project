package com.sinergia.eLibrary.presentation.Account.Presenter

import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.interactors.AccountInteractor.AccountInteractor
import com.sinergia.eLibrary.presentation.Account.AccountContract
import com.sinergia.eLibrary.presentation.Account.Model.AccountViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AccountPresenter(accountViewModel: AccountViewModel, accountInteractor: AccountInteractor): AccountContract.AccountPresenter, CoroutineScope {

    var view: AccountContract.AccountView ?= null
    var accountViewModel: AccountViewModel ?= null
    var accountInteractor: AccountInteractor ?= null

    private val accountJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + accountJob

    init{
        this.accountViewModel = accountViewModel
        this.accountInteractor = accountInteractor
    }


    override fun attachView(view: AccountContract.AccountView) {
        this.view = view
    }

    override fun dettachView() {
        this.view = null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun isViewAttach(): Boolean {
        return this.view !== null
    }

    override fun logOut() {
        launch{
            accountInteractor?.logOut()
            if(isViewAttach()) view?.navigateToMainPage()
        }
    }

    override fun updateAccount(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAccount(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}