package com.sinergia.eLibrary.presentation.Account

import com.sinergia.eLibrary.data.Model.User

interface AccountContract {

    interface AccountView{

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun enableAllButtons()
        fun disableAllButtons()

        fun logOut()
        fun updateAccount()
        fun deleteAccount()

        fun refresh()
        fun navigateToMainPage()

    }

    interface AccountPresenter{

        fun attachView(view: AccountView)
        fun dettachView()
        fun dettachJob()
        fun isViewAttach(): Boolean

        fun logOut()
        fun updateAccount(user: User)
        fun deleteAccount(user: User)

    }

}