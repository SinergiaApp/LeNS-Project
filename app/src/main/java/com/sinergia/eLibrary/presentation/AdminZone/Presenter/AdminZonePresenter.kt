package com.sinergia.eLibrary.presentation.AdminZone.Presenter

import com.google.firebase.firestore.GeoPoint
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

    //ADD RESOURCE METHODS
    override fun checkEmptyAddResourceFields(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String): Boolean {
        return checkEmptyAddResourceTitle(titulo) || checkEmptyAddResourceAuthor(autor) || checkEmptyAddResourceEdition(edicion) || checkEmptyAddResourceIBAN(iban) || checkEmptyAddResourceSinopsis(sinopsis)
    }

    override fun checkEmptyAddResourceTitle(titulo: String): Boolean {
        return titulo.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceAuthor(autor: String): Boolean {
        return autor.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceIBAN(isbn: String): Boolean {
        return isbn.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceEdition(edicion: String): Boolean {
        return edicion.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceSinopsis(sinopsis: String): Boolean {
        return sinopsis.isNullOrEmpty()
    }

    override fun checkValidIBAN(isbn: String): Boolean {
        val isbn_regex: String = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
        val isbn_pattern = Pattern.compile(isbn_regex)
        return isbn_pattern.matcher(isbn).matches()
    }

    override fun addNewResource(titulo: String, autor: String, isbn: String, edicion: String, sinopsis: String) {

        view?.showAddResourceProgressBar()
        view?.disableAddResourceButton()

        adminViewModel?.addNewResource(titulo, autor, isbn, edicion, sinopsis, object: AdminViewModel.createResourceCallBack{
            override fun onCreateResourceSuccess() {
                view?.hideAddResourceProgressBar()
                view?.enableAddResourceButton()
                view?.showMessage("El recurso se ha creado satisfactoriamente.")
                view?.navigateToMainPage()
            }

            override fun onCreateResourceFailure(errorMsg: String) {
                view?.hideAddResourceProgressBar()
                view?.enableAddResourceButton()
                view?.showError(errorMsg)
            }


        })

    }

    //ADD LIBRARY METHODS
    override fun checkEmptyAddLibraryFields(nombre: String, direccion: String, latitud: Double, longitud: Double): Boolean {
        return checkEmptyAddLibraryName(nombre) || checkEmptyAddLibraryAddress(direccion) || checkWrongAddLibraryLatitude(latitud) || checkWrongAddLibraryLongitude(longitud)
    }

    override fun checkEmptyAddLibraryName(nombre: String): Boolean {
        return nombre.isNullOrEmpty()
    }

    override fun checkEmptyAddLibraryAddress(direccion: String): Boolean {
        return direccion.isNullOrEmpty()
    }

    override fun checkWrongAddLibraryLatitude(latitud: Double): Boolean {
        return latitud.isNaN()
    }

    override fun checkWrongAddLibraryLongitude(longitud: Double): Boolean {
        return longitud.isNaN()
    }

    override fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint) {

        view?.showAddLibraryProgressBar()
        view?.disableAddLibraryButton()

        adminViewModel?.addNewLibrary(nombre, direccion, geopoint, object: AdminViewModel.createLibrarylCallBack{
            override fun onCreateLibrarySuccess() {
                view?.hideAddLibraryProgressBar()
                view?.enableAddLibraryButton()
                view?.showMessage("La Biblioteca se ha creado satisfactoriamente.")
                view?.navigateToMainPage()
            }

            override fun onCreateLibraryFailure(errorMsg: String) {
                view?.hideAddLibraryProgressBar()
                view?.enableAddLibraryButton()
                view?.showError(errorMsg)
            }

        })

    }

}