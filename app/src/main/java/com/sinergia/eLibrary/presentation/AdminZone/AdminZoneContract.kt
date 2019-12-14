package com.sinergia.eLibrary.presentation.AdminZone

import com.google.firebase.firestore.GeoPoint


interface AdminZoneContract {

    interface AdminZoneView {

        fun showError(error: String)
        fun showMessage(message: String)

        fun showHideAddResource()
        fun showAddResourceProgressBar()
        fun hideAddResourceProgressBar()
        fun enableAddResourceButton()
        fun disableAddResourceButton()
        fun createNewResource()

        fun showHideAddLibrary()
        fun showAddLibraryProgressBar()
        fun hideAddLibraryProgressBar()
        fun enableAddLibraryButton()
        fun disableAddLibraryButton()
        fun createNewLibrary()

        fun navigateToMainPage()

    }

    interface AdminZonePresenter{

        fun attachView(view: AdminZoneView)
        fun dettachView()
        fun isViewAttach(): Boolean

        fun addNewResource(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String)
        fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint)

        fun checkEmptyAddResourceFields(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String): Boolean
        fun checkEmptyAddResourceTitle(titulo: String): Boolean
        fun checkEmptyAddResourceAuthor(autor: String): Boolean
        fun checkEmptyAddResourceIBAN(iban: String): Boolean
        fun checkEmptyAddResourceEdition(edicion: String): Boolean
        fun checkEmptyAddResourceSinopsis(sinopsis: String): Boolean

        fun checkEmptyAddLibraryFields(nombre: String, direccion: String, latitud: String, longitud: String): Boolean
        fun checkEmptyAddLibraryName(nombre: String): Boolean
        fun checkEmptyAddLibraryAddress(direccion: String): Boolean
        fun checkEmptyAddLibraryLatitude(latitud: String): Boolean
        fun checkEmptyAddLibraryLongitude(longitud: String): Boolean

        fun checkValidIBAN(iban: String): Boolean

    }

}