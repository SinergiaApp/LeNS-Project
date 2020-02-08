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

        fun showHideSetResource()
        fun showSetResourceProgressBar()
        fun hideSetResourceProgressBar()
        fun enableSearchResourceToModifyButton()
        fun disableSearchResourceToModifyButton()
        fun enableSetResourceButton()
        fun disableSetResourceButton()
        fun showSetResouceContent()
        fun getResourceToModify()
        fun initSetResourceContent(resource: Resource?, libraries: ArrayList<Library>?)
        fun setResource()

        fun showHideAddLibrary()
        fun showAddLibraryProgressBar()
        fun hideAddLibraryProgressBar()
        fun enableAddLibraryButton()
        fun disableAddLibraryButton()
        fun createNewLibrary()

        fun showHideSetLibrary()
        fun showSetLibraryProgressBar()
        fun hideSetLibraryProgressBar()
        fun enableSearchLibraryToModifyButton()
        fun disableSearchLibraryToModifyButton()
        fun enableSetLibraryButton()
        fun disableSetLibraryButton()
        fun showSetLibraryContent()
        fun getLibraryToModify()
        fun initLibraryContent(library: Library?)
        fun setLibrary()

        fun clickOnCamera(field: String)
        fun startScan()
        fun checkAndSetCamentaPermissions()

        fun navigateToCatalog()
        fun navigateToLibraries()

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
            dislikes: MutableList<String>,
            isOnline: Boolean,
            urlOnline: String
        )

        fun getResourceToModify(isbn: String)
        fun setResource(resource: Resource)

        fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint)

        fun getLibraryToModify(id: String)
        fun setLibrary(library: Library)

        fun checkEmptyAddResourceFields(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String): Boolean
        fun checkEmptyAddResourceTitle(titulo: String): Boolean
        fun checkEmptyAddResourceAuthor(autor: String): Boolean
        fun checkEmptyAddResourceISBN(iban: String): Boolean
        fun checkEmptyAddResourceEdition(edicion: String): Boolean
        fun checkEmptyAddResourcePublisher(editorial: String): Boolean
        fun checkEmptyAddResourceSinopsis(sinopsis: String): Boolean
        fun checkEmptyAddResourceIsOnline(isOnline: Boolean, urlOnline: String): Boolean

        fun checkEmptySetResourceFields(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String): Boolean
        fun checkEmptySetResourceTitle(titulo: String): Boolean
        fun checkEmptySetResourceAuthor(autor: String): Boolean
        fun checkEmptySetResourceISBN(iban: String): Boolean
        fun checkEmptySetResourceEdition(edicion: String): Boolean
        fun checkEmptySetResourcePublisher(editorial: String): Boolean
        fun checkEmptySetResourceSinopsis(sinopsis: String): Boolean
        fun checkEmptySetResourceIsOnline(isOnline: Boolean, urlOnline: String): Boolean


        fun checkEmptyAddLibraryFields(nombre: String, direccion: String, latitud: String, longitud: String): Boolean
        fun checkEmptyAddLibraryName(nombre: String): Boolean
        fun checkEmptyAddLibraryAddress(direccion: String): Boolean
        fun checkEmptyAddLibraryLatitude(latitud: String): Boolean
        fun checkEmptyAddLibraryLongitude(longitud: String): Boolean
        fun checkInRangeAddLibraryGeopoints(latitud: String, longitud: String): Boolean
        fun checkInRangeAddLibraryLatitude(latitud: String): Boolean
        fun checkInRangeAddLibraryLongitude(longitud: String): Boolean

        fun checkEmptySetLibraryFields(nombre: String, direccion: String, latitud: String, longitud: String): Boolean
        fun checkEmptySetLibraryName(nombre: String): Boolean
        fun checkEmptySetLibraryAddress(direccion: String): Boolean
        fun checkEmptySetLibraryLatitude(latitud: String): Boolean
        fun checkEmptySetLibraryLongitude(longitud: String): Boolean
        fun checkInRangeSetLibraryGeopoints(latitud: String, longitud: String): Boolean
        fun checkInRangeSetLibraryLatitude(latitud: String): Boolean
        fun checkInRangeSetLibraryLogitude(longitud: String): Boolean

        fun checkValidISBN(isbn: String): Boolean

    }

}