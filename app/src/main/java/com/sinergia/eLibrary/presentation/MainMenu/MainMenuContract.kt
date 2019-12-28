package com.sinergia.eLibrary.presentation.MainMenu

interface MainMenuContract {

    interface MainContractView {

        fun showMessage(message: String?)
        fun showError(errorMsg: String?)
        fun goToLibrary()
        fun goToCatalog()
        fun goToNeurolinguisticZone()
        fun goToAccount()
        fun goToAdminZone()

        fun disableAllButtons()
        fun enableAllButtons()

        fun showHideAdminZone(admin: Boolean)

    }

    interface MainContractPresenter {

        fun attachiew(view: MainContractView)
        fun dettachView()
        fun isViewAttached(): Boolean
        fun dettachJob()

        fun getCurrentUserDB(email: String)

    }

}