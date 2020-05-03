package com.sinergia.eLibrary.presentation.Account

import android.net.Uri
import com.sinergia.eLibrary.data.Model.Loan
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.User

interface AccountContract {

    interface AccountView{

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun enableAllButtons()
        fun disableAllButtons()
        fun initAccountContent()

        fun logOut()
        fun updateAccount()
        fun deleteAccount()

        fun navigateToMainPage()

        fun checkAndSetGalleryPermissions()
        fun uploadGalleryImage()

    }

    interface AccountPresenter{

        fun attachView(view: AccountView)
        fun dettachView()
        fun dettachJob()
        fun isViewAttach(): Boolean

        fun userCanBeDeleted(reserves: MutableList<String>, loans: MutableList<String>): Boolean
        fun checkEmptyAcountReserves(reserves: MutableList<String>): Boolean
        fun checkEmptyAcountLoans(loans: MutableList<String>): Boolean

        fun logOut()
        fun updateAccount(newUserAccount: User)
        fun deleteAccount(user: User)

        fun uploadImage(imageURI: Uri)

    }

}