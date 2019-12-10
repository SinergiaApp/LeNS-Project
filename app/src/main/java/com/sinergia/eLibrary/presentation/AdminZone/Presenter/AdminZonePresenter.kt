package com.sinergia.eLibrary.presentation.AdminZone.Presenter

import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl
import java.util.regex.Pattern

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

    override fun checkEmptyIBAN(isbn: String): Boolean {
        return isbn.isNullOrEmpty()
    }

    override fun checkEmptyEdition(edicion: String): Boolean {
        return edicion.isNullOrEmpty()
    }

    override fun checkEmptySinopsis(sinopsis: String): Boolean {
        return sinopsis.isNullOrEmpty()
    }

    override fun checkValidIBAN(isbn: String): Boolean {
        val isbn_regex: String = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
        val isbn_pattern = Pattern.compile(isbn_regex)
        return isbn_pattern.matcher(isbn).matches()
    }

    override fun addNewResource(titulo: String, autor: String, isbn: String, edicion: String, sinopsis: String) {

        view?.showProgressBar()
        view?.disableAddResourceButton()

        adminViewModel?.addNewResource(titulo, autor, isbn, edicion, sinopsis, object: AdminViewModel.AdminViewModelCallBack{
            override fun onCreateResourceSuccess() {
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