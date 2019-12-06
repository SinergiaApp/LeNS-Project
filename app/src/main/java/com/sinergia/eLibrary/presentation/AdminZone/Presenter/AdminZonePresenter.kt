package com.sinergia.eLibrary.presentation.AdminZone.Presenter

import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl

class AdminZonePresenter(adminViewModel: AdminViewModelImpl): AdminZoneContract.AdminZonePresenter {

    var view: AdminZoneContract.AdminZoneView? = null
    var adminViewModel: AdminViewModelImpl? = null

    init {
        this.adminViewModel = adminViewModel
    }

    override fun attachView(view: AdminZoneContract.AdminZoneView) {
        this.view = view
    }

    override fun dettachView() {
        view = null
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun checkEmptyFields(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String): Boolean {
        return checkEmptyTitle(titulo) || checkEmptyAuthor(autor) || checkEmptyEdition(edicion) || checkEmptyIBAN(iban) || checkEmptySinopsis(sinopsis)
    }

    override fun checkEmptyTitle(titulo: String): Boolean {
        return titulo.isNullOrEmpty()
    }

    override fun checkEmptyAuthor(autor: String): Boolean {
        return autor.isNullOrEmpty()
    }

    override fun checkEmptyIBAN(iban: String): Boolean {
        return iban.isNullOrEmpty()
    }

    override fun checkEmptyEdition(edicion: String): Boolean {
        return edicion.isNullOrEmpty()
    }

    override fun checkEmptySinopsis(sinopsis: String): Boolean {
        return sinopsis.isNullOrEmpty()
    }

    override fun checkValidIBAN(iban: String): Boolean {
        //TODO: COLOCAR AQUÍ UNA EXPRESIÓN REGULAR PARA CHEQUEAR QUE ESTÉ BIEN INTRODUCIDO UN DETERMINADO IBAN
        return true
    }

    override fun addNewResource(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String) {

        view?.showProgressBar()
        view?.disableAddResourceButton()

        adminViewModel?.addNewResource(titulo, autor, iban, edicion, sinopsis, object: AdminViewModel.AdminViewModelCallBack{
            override fun onCreateResourceSucces() {
                view?.hideProgressBar()
                view?.enableAddResourceButton()
                view?.showMessage("El recurso se ha creado satisfactoriamente.")
                view?.navigateToMainPage()
            }

            override fun onCreateResourceFailure(errorMsg: String) {
                view?.hideProgressBar()
                view?.enableAddResourceButton()
                view?.showError(errorMsg)
            }


        })



    }

}