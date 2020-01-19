package com.sinergia.eLibrary.presentation.AdminZone

import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource


interface AdminZoneContract {

    interface AdminZoneView {

        fun showError(error: String?)
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
        fun dettachJob()

        fun addNewResource(
            titulo: String,
            autor: List<String>,
            isbn: String,
            edicion: String,
            editorial: String,
            sinopsis: String,
            librariesDisponibility: MutableMap<String, Integer>,
            likes: MutableList<String>,
            dislikes: MutableList<String>
        )
        fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint)

        fun checkEmptyAddResourceFields(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String): Boolean
        fun checkEmptyAddResourceTitle(titulo: String): Boolean
        fun checkEmptyAddResourceAuthor(autor: String): Boolean
        fun checkEmptyAddResourceISBN(iban: String): Boolean
        fun checkEmptyAddResourceEdition(edicion: String): Boolean
        fun checkEmptyAddResourcePublisher(editorial: String): Boolean
        fun checkEmptyAddResourceSinopsis(sinopsis: String): Boolean

        fun checkEmptyAddLibraryFields(nombre: String, direccion: String, latitud: String, longitud: String): Boolean
        fun checkEmptyAddLibraryName(nombre: String): Boolean
        fun checkEmptyAddLibraryAddress(direccion: String): Boolean
        fun checkEmptyAddLibraryLatitude(latitud: String): Boolean
        fun checkEmptyAddLibraryLongitude(longitud: String): Boolean

        fun checkValidISBN(iban: String): Boolean

    }

}