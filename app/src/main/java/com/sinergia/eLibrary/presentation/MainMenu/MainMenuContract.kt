package com.sinergia.eLibrary.presentation.MainMenu

interface MainMenuContract {

    interface MainContractView {

        fun goToLibrary()
        fun goToCatalog()
        fun goToNeurolinguisticZone()
        fun goToAccount()
        fun goToAdminZone()

        fun disableAllButtons()
        fun enableAllButtons()

    }

}