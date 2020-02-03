package com.sinergia.eLibrary.presentation.AdminZone.Presenter

import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.base.Exceptions.FirebaseCreateLibraryException
import com.sinergia.eLibrary.base.Exceptions.FirebaseCreateResourceException
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetAllLibrariesException
import com.sinergia.eLibrary.base.Exceptions.FirebaseSetResourceException
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl
import kotlinx.coroutines.*
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext

class AdminZonePresenter(adminViewModel: AdminViewModelImpl): AdminZoneContract.AdminZonePresenter, CoroutineScope {

    val TAG = "[ADMIN_ACTIVITY]"

    var view: AdminZoneContract.AdminZoneView? = null
    var adminViewModel: AdminViewModelImpl? = null
    private val adminJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + adminJob

    init {
        this.adminViewModel = adminViewModel
    }

    //PRESENTER MAIN METHODS
    override fun attachView(view: AdminZoneContract.AdminZoneView) {
        this.view = view
    }

    override fun dettachView() {
        view = null
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    //ADD RESOURCE METHODS
    override fun checkEmptyAddResourceFields(
        titulo: String,
        autor: String,
        isbn: String,
        edicion: String,
        editorial: String,
        sinopsis: String): Boolean {
        return (
            checkEmptyAddResourceTitle(titulo) ||
            checkEmptyAddResourceAuthor(autor) ||
            checkEmptyAddResourceEdition(edicion) ||
            checkEmptyAddResourcePublisher(editorial) ||
            checkEmptyAddResourceISBN(isbn) ||
            checkEmptyAddResourceSinopsis(sinopsis)
        )
    }

    override fun checkEmptyAddResourceTitle(titulo: String): Boolean {
        return titulo.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceAuthor(autor: String): Boolean {
        return autor.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceISBN(isbn: String): Boolean {
        return isbn.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceEdition(edicion: String): Boolean {
        return edicion.isNullOrEmpty()
    }

    override fun checkEmptyAddResourcePublisher(editorial: String): Boolean {
        return editorial.isNullOrEmpty()
    }

    override fun checkEmptyAddResourceSinopsis(sinopsis: String): Boolean {
        return sinopsis.isNullOrEmpty()
    }

    override fun checkValidISBN(isbn: String): Boolean {
        val isbn_regex: String = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+[- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$"
        val isbn_pattern = Pattern.compile(isbn_regex)
        return isbn_pattern.matcher(isbn).matches()
    }

    override fun checkEmptyAddResourceIsOnline(isOnline: Boolean, urlOnline: String): Boolean {
        if(isOnline){
            return urlOnline.isNullOrEmpty()
        } else {
            return false
        }
    }

    override fun addNewResource(
        titulo: String,
        autores: List<String>,
        isbn: String,
        edicion: String,
        editorial: String,
        sinopsis: String,
        librariesDisponibility: MutableMap<String, Integer>,
        likes: MutableList<String>,
        dislikes: MutableList<String>,
        isOnline: Boolean,
        urlOnline: String) {

        launch {

            view?.showAddResourceProgressBar()
            view?.disableAddResourceButton()

            try {
                adminViewModel?.addNewResource(titulo, autores, isbn, edicion, editorial, sinopsis, librariesDisponibility, likes, dislikes, isOnline, urlOnline)

                if(isViewAttach()){
                    view?.hideAddResourceProgressBar()
                    view?.enableAddResourceButton()
                    view?.showMessage("El Recurso se ha creado satisfactoriamente.")
                    view?.navigateToMainPage()
                }

                Log.d(TAG, "Succesfully create new Resource.")

            } catch (error: FirebaseCreateResourceException){

                val errorMsg = error.message
                view?.showError(errorMsg)
                view?.hideAddResourceProgressBar()
                view?.enableAddResourceButton()

                Log.d(TAG, "ERROR: Cannot create new Resource with name $titulo --> $errorMsg.")
            }

        }

    }

    //SET RESOURCE METHODS
    override fun checkEmptySetResourceFields(
        titulo: String,
        autor: String,
        isbn: String,
        edicion: String,
        editorial: String,
        sinopsis: String
    ): Boolean {
        return (
            checkEmptySetResourceTitle(titulo) ||
            checkEmptySetResourceAuthor(autor) ||
            checkEmptySetResourceEdition(edicion) ||
            checkEmptySetResourcePublisher(editorial) ||
            checkEmptySetResourceISBN(isbn) ||
            checkEmptySetResourceSinopsis(sinopsis)
        )
    }

    override fun checkEmptySetResourceTitle(titulo: String): Boolean {
        return titulo.isNullOrEmpty()
    }

    override fun checkEmptySetResourceAuthor(autor: String): Boolean {
        return autor.isNullOrEmpty()
    }

    override fun checkEmptySetResourceISBN(isbn: String): Boolean {
        return isbn.isNullOrEmpty()
    }

    override fun checkEmptySetResourceEdition(edicion: String): Boolean {
        return edicion.isNullOrEmpty()
    }

    override fun checkEmptySetResourcePublisher(editorial: String): Boolean {
        return editorial.isNullOrEmpty()
    }

    override fun checkEmptySetResourceSinopsis(sinopsis: String): Boolean {
        return sinopsis.isNullOrEmpty()
    }

    override fun checkEmptySetResourceIsOnline(isOnline: Boolean, urlOnline: String): Boolean {
        if(isOnline) {
          return urlOnline.isNullOrEmpty()
        } else {
            return false
        }
    }

    override fun getResourceToModify(isbn: String) {
        launch{

            Log.d(TAG, "Trying to get Resource with isbn $isbn.")
            view?.showSetResourceProgressBar()
            view?.disableSearchResourceToModifyButton()

            try{
                val resource = adminViewModel?.getResourceToModify(isbn)
                val libraries = adminViewModel?.getAllLibraries()
                if(isViewAttach()){
                    view?.hideSetResourceProgressBar()
                    view?.enableSearchResourceToModifyButton()
                    view?.enableSetResourceButton()
                    view?.initSetResourceContent(resource, libraries)
                    view?.showMessage("El recurso está listo para sermodificado.")
                }
            } catch (error: FirebaseSetResourceException){
                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetResourceProgressBar()
                    view?.enableSearchResourceToModifyButton()
                    view?.disableSetResourceButton()
                }

                Log.d(TAG, "ERROR: Cannot get Resource with isbn $isbn--> $errorMsg.")

            }

        }

    }

    override fun setResource(resource: Resource) {

        launch{

            val titulo = resource.title
            Log.d(TAG, "Trying to set Resource with name $titulo.")
            view?.showSetResourceProgressBar()
            view?.disableSetResourceButton()

            try{
                adminViewModel?.setResource(resource)
                if(isViewAttach()){
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButton()
                    view?.showMessage("El recurso se ha modificado satisfactoriamente.")
                    view?.navigateToMainPage()

                }
            } catch (error: FirebaseSetResourceException){
                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButton()
                }

                Log.d(TAG, "ERROR: Cannot modify Resource with name $titulo --> $errorMsg.")

            }

        }

    }

    //ADD LIBRARY METHODS
    override fun checkEmptyAddLibraryFields(nombre: String, direccion: String, latitud: String, longitud: String): Boolean {
        return checkEmptyAddLibraryName(nombre) || checkEmptyAddLibraryAddress(direccion) || checkEmptyAddLibraryLatitude(latitud) || checkEmptyAddLibraryLongitude(longitud)
    }

    override fun checkEmptyAddLibraryName(nombre: String): Boolean {
        return nombre.isNullOrEmpty()
    }

    override fun checkEmptyAddLibraryAddress(direccion: String): Boolean {
        return direccion.isNullOrEmpty()
    }

    override fun checkEmptyAddLibraryLatitude(latitud: String): Boolean {
        return latitud.isNullOrEmpty()
    }

    override fun checkEmptyAddLibraryLongitude(longitud: String): Boolean {
        return longitud.isNullOrEmpty()
    }

    override fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint) {

        launch {

            Log.d(TAG, "Trying to create new LibraryActivity with name $nombre.")
            view?.showAddLibraryProgressBar()
            view?.disableAddLibraryButton()

            try {

                adminViewModel?.addNewLibrary(nombre, direccion, geopoint)
                if(isViewAttach()) {
                    view?.hideAddLibraryProgressBar()
                    view?.enableAddLibraryButton()
                    view?.showMessage("La Biblioteca se ha creado satisfactoriamente.")
                    view?.navigateToMainPage()
                }
                Log.d(TAG, "Succesfully creates new LibraryActivity.")

            } catch (error: FirebaseCreateLibraryException){

                val errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideAddLibraryProgressBar()
                    view?.enableAddLibraryButton()
                }

                Log.d(TAG, "ERROR: Cannot create new LibraryActivity with name $nombre --> $errorMsg.")

            }

        }

    }

}