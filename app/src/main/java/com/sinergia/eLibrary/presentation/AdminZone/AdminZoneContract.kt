package com.sinergia.eLibrary.presentation.AdminZone


interface AdminZoneContract {

    interface AdminZoneView {

        fun showHideAddResource()

        fun showError(error: String)
        fun showMessage(message: String)
        fun showProgressBar()
        fun hideProgressBar()
        fun enableAddResourceButton()
        fun disableAddResourceButton()
        fun createNewResource()
        fun navigateToMainPage()

    }

    interface AdminZonePresenter{

        fun attachView(view: AdminZoneView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun addNewResource(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String)

        fun checkEmptyFields(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String): Boolean
        fun checkEmptyTitle(titulo: String): Boolean
        fun checkEmptyAuthor(autor: String): Boolean
        fun checkEmptyIBAN(iban: String): Boolean
        fun checkEmptyEdition(edicion: String): Boolean
        fun checkEmptySinopsis(sinopsis: String): Boolean

        fun checkValidIBAN(iban: String): Boolean

    }

}