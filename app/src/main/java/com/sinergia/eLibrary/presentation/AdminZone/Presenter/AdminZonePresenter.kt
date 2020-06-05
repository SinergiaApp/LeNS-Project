package com.sinergia.eLibrary.presentation.AdminZone.Presenter

import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.base.Exceptions.*
import com.sinergia.eLibrary.data.Model.*
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.coroutines.*
import java.time.LocalDateTime
import java.util.regex.Pattern
import kotlin.coroutines.CoroutineContext

class AdminZonePresenter(adminViewModel: AdminViewModelImpl): AdminZoneContract.AdminZonePresenter, CoroutineScope {

    val TAG = "[ADMIN_ACTIVITY]"

    var view: AdminZoneContract.AdminZoneView? = null
    var adminViewModel: AdminViewModelImpl? = null
    private val adminJob = Job()

    private lateinit var currentLibrary: Library
    private lateinit var currentResource: Resource

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
        librariesDisponibility: MutableMap<String, Int>,
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
                    view?.inforNewResourcemWithDialog("¡El recurso ha sido creado!. \nRecuerda, es necesario que modifiques " +
                            "la imagen que se muestra en la ficha del recurso. Para ello, debes dirigirte a la edición " +
                            "de recursos en la zona de gerencia, buscar el recurso que acabas de crear y modificar su imagen.")
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

                currentResource = adminViewModel?.getResourceToModify(isbn)!!
                NeLSProject.currentResource = currentResource
                val libraries = adminViewModel?.getAllLibraries()

                if(isViewAttach()){
                    view?.hideSetResourceProgressBar()
                    view?.enableSearchResourceToModifyButton()
                    view?.enableSetResourceButtons()
                    view?.showSetResouceContent()
                    view?.initSetResourceContent(currentResource, libraries)
                    view?.showMessage("El recurso está listo para sermodificado.")

                }

                Log.d(TAG, "Succesfully getted resource to modify with isbn $isbn.")

            } catch (error: FirebaseGetResourceException){
                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetResourceProgressBar()
                    view?.enableSearchResourceToModifyButton()
                    view?.disableSetResourceButtons()
                }

                Log.d(TAG, "ERROR: Cannot get Resource with isbn $isbn--> $errorMsg.")

            }

        }

    }

    override fun setResource(resource: Resource) {

        launch{

            Log.d(TAG, "Trying to set Resource with isbn ${resource.isbn}.")
            if(isViewAttach()){
                view?.showSetResourceProgressBar()
                view?.disableSetResourceButtons()
            }

            try{

                adminViewModel?.setResource(resource)

                if(isViewAttach()){
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButtons()
                    view?.showMessage("El recurso se ha modificado satisfactoriamente.")
                    view?.navigateToCatalog()

                }

                Log.d(TAG, "Succesfully Modified Resource with isbn ${resource.isbn}.")

            } catch (error: FirebaseSetResourceException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButtons()
                }

                Log.d(TAG, "ERROR: Cannot modify Resource with isbn ${resource.isbn} --> $errorMsg.")

            }

        }

    }

    override fun deleteResource(deletedResource: Resource) {

        launch{

            Log.d(TAG, "Trying to delete Resource with isbn ${deletedResource.isbn}.")
            if(isViewAttach()){
                view?.showSetResourceProgressBar()
                view?.disableSetResourceButtons()
            }

            try{

                adminViewModel?.deleteResource(deletedResource)

                if(isViewAttach()){
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButtons()
                    view?.showMessage("El recurso se ha eliminado satisfactoriamente.")
                    view?.navigateToCatalog()

                }

                Log.d(TAG, "Succesfully deleted Resource with isbn ${deletedResource.isbn}.")

            } catch (error: FirebaseSetResourceException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButtons()
                }

                Log.d(TAG, "ERROR: Cannot delete Resource with isbn ${deletedResource.isbn} --> $errorMsg.")

            }

        }

    }

    override fun setResourceImage(resourceImageUri: Uri) {

        launch{

            Log.d(TAG, "Trying to set Resource with isbn ${currentResource.isbn}.")
            if(isViewAttach()){
                view?.showSetResourceProgressBar()
                view?.disableSetResourceButtons()
            }

            try{

                val newResourceImageURL = adminViewModel?.setResourceImage(currentResource.isbn, resourceImageUri)
                currentResource.imageUri = newResourceImageURL.toString()
                adminViewModel?.setResource(currentResource)

                if(isViewAttach()){
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButtons()
                    view?.showMessage("La imagen del Recurso se ha modificado satisfactoriamente.")
                    view?.navigateToCatalog()
                }

                Log.d(TAG, "Successfully modified resource image with isbn ${currentResource.isbn}")

            } catch (error: FirebaseSetResourceException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetResourceProgressBar()
                    view?.enableSetResourceButtons()
                }

                Log.d(TAG, "ERROR: Cannot modify Resource  with isbn ${currentResource.isbn} --> $errorMsg.")

            } catch (error: FirebaseStorageUploadImageException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetLibraryProgressBar()
                    view?.enableSearchLibraryToModifyButton()
                    view?.enableSetLibraryButtons()
                }

                Log.d(TAG, "ERROR: Cannot set Resource Image with isbn ${currentResource.isbn} --> $errorMsg.")

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

    override fun checkInRangeAddLibraryGeopoints(latitud: String, longitud: String): Boolean {
        return checkInRangeAddLibraryLatitude(latitud) || checkInRangeAddLibraryLongitude(longitud)
    }

    override fun checkInRangeAddLibraryLatitude(latitud: String): Boolean {
        return latitud.toDouble() < -180.0 || latitud.toDouble() > 180.0
    }

    override fun checkInRangeAddLibraryLongitude(longitud: String): Boolean {
        return longitud.toDouble() < -180.0 || longitud.toDouble() > 180.0
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
                    view?.inforNewLibrarymWithDialog("¡La biblioteca ha sido creada!. \nRecuerda, es necesario que modifiques " +
                            "la imagen que se muestra en la ficha de la biblioteca. Para ello, debes dirigirte a la edición " +
                            "de bibliotecas en la zona de gerencia, buscar la biblioteca que acabas de crear y modificar su imagen.")

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

    // SET LIBRARY METHODS
    override fun checkEmptySetLibraryFields(
        nombre: String,
        direccion: String,
        latitud: String,
        longitud: String
    ): Boolean {
        return (
            checkEmptySetLibraryName(nombre) ||
            checkEmptySetLibraryAddress(direccion) ||
            checkEmptySetLibraryLatitude(latitud) ||
            checkEmptySetLibraryLongitude(longitud)
        )
    }

    override fun checkEmptySetLibraryName(nombre: String): Boolean {
        return nombre.isNullOrEmpty()
    }

    override fun checkEmptySetLibraryAddress(direccion: String): Boolean {
        return direccion.isNullOrEmpty()
    }

    override fun checkEmptySetLibraryLatitude(latitud: String): Boolean {
        return latitud.isNullOrEmpty()
    }

    override fun checkEmptySetLibraryLongitude(longitud: String): Boolean {
        return longitud.isNullOrEmpty()
    }

    override fun checkInRangeSetLibraryGeopoints(latitud: String, longitud: String): Boolean {
        return checkInRangeSetLibraryLatitude(latitud) || checkInRangeAddLibraryLongitude(longitud)
    }

    override fun checkInRangeSetLibraryLatitude(latitud: String): Boolean {
        return latitud.toDouble() < -180.0 || latitud.toDouble() > 180.0
    }

    override fun checkInRangeSetLibraryLogitude(longitud: String): Boolean {
        return longitud.toDouble() < -180.0 || longitud.toDouble() > 180.0
    }

    override fun getLibraryToModify(id: String) {

        Log.d(TAG, "Trying to get library with id $id.")
        view?.showSetLibraryProgressBar()
        view?.disableSearchLibraryToModifyButton()
        view?.disableSetLibraryButtons()

        launch{

            try{

                currentLibrary = adminViewModel?.getLibraryToModify(id)!!
                NeLSProject.currentLibrary = currentLibrary

                if(isViewAttach()) {
                    view?.hideSetLibraryProgressBar()
                    view?.enableSearchLibraryToModifyButton()
                    view?.enableSetLibraryButtons()
                    view?.showSetLibraryContent()
                    view?.initLibraryContent(currentLibrary)
                    view?.showMessage("La biblioteca está lista para ser modificada.")
                }

                Log.d(TAG, "Succesfully getted library to modify with id $id.")

            } catch(error: FirebaseGetLibraryException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetLibraryProgressBar()
                    view?.enableSearchLibraryToModifyButton()
                    view?.disableSetLibraryButtons()
                }

                Log.d(TAG, "ERROR: Cannot get Library with id $id--> $errorMsg.")
            }

        }


    }

    override fun setLibrary(library: Library) {

        Log.d(TAG, "Trying to modify Library with id ${library.id}.")
        view?.showSetLibraryProgressBar()
        view?.disableSearchLibraryToModifyButton()
        view?.disableSetLibraryButtons()

        launch{

            try {

                adminViewModel?.setLibrary(library)

                if(isViewAttach()){
                    view?.hideSetLibraryProgressBar()
                    view?.enableSetLibraryButtons()
                    view?.showMessage("La Biblioteca se ha modificado satisfactoriamente.")
                    view?.navigateToLibraries()
                }

                Log.d(TAG, "Succesfully Modified Library with id ${library.id}.")

            } catch (error: FirebaseSetLibraryException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetLibraryProgressBar()
                    view?.enableSearchLibraryToModifyButton()
                    view?.enableSetLibraryButtons()
                }

                Log.d(TAG, "ERROR: Cannot set Library with id ${library.id}--> $errorMsg.")

            }

        }

    }

    override fun deleteLibrary(deletedLibrary: Library) {

        Log.d(TAG, "Trying to delete Library with id ${deletedLibrary.id}.")
        view?.showSetLibraryProgressBar()
        view?.disableSearchLibraryToModifyButton()
        view?.disableSetLibraryButtons()

        launch{

            try {

                adminViewModel?.deleteLibrary(deletedLibrary)

                if(isViewAttach()){
                    view?.hideSetLibraryProgressBar()
                    view?.enableSetLibraryButtons()
                    view?.showMessage("La Biblioteca se ha eliminado satisfactoriamente.")
                    view?.navigateToLibraries()
                }

                Log.d(TAG, "Succesfully deleted Library with id ${deletedLibrary.id}.")

            } catch (error: FirebaseSetLibraryException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetLibraryProgressBar()
                    view?.enableSearchLibraryToModifyButton()
                    view?.enableSetLibraryButtons()
                }

                Log.d(TAG, "ERROR: Cannot delete Library with id ${deletedLibrary.id}--> $errorMsg.")

            }

        }

    }

    override fun setLibraryImage(libraryImageUri: Uri) {

        Log.d(TAG, "Trying to modify Library Image with id ${currentLibrary.id}.")
        view?.showSetLibraryProgressBar()
        view?.disableSearchLibraryToModifyButton()
        view?.disableSetLibraryButtons()

        launch{

            try {

                if(isViewAttach()){
                    view?.showSetLibraryProgressBar()
                    view?.disableSearchLibraryToModifyButton()
                    view?.disableSetLibraryButtons()
                }

                val newLibraryImageUri = adminViewModel?.setLibraryImage(currentLibrary.id, libraryImageUri)
                currentLibrary.imageUri = newLibraryImageUri.toString()
                adminViewModel?.setLibrary(currentLibrary)

                if(isViewAttach()){
                    view?.hideSetLibraryProgressBar()
                    view?.enableSetLibraryButtons()
                    view?.showMessage("La imagen de la Biblioteca se ha modificado satisfactoriamente.")
                    view?.navigateToLibraries()
                }

                Log.d(TAG, "Successfully modified library image with isbn ${currentLibrary.id}")

            } catch (error: FirebaseSetLibraryException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetLibraryProgressBar()
                    view?.enableSearchLibraryToModifyButton()
                    view?.enableSetLibraryButtons()
                }

                Log.d(TAG, "ERROR: Cannot set Library with id ${currentLibrary.id}--> $errorMsg.")

            } catch (error: FirebaseStorageUploadImageException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideSetLibraryProgressBar()
                    view?.enableSearchLibraryToModifyButton()
                    view?.enableSetLibraryButtons()
                }

                Log.d(TAG, "ERROR: Cannot set Library Image with id ${currentLibrary.id}--> $errorMsg.")

            }

        }

    }

    // LOAN AND RESERVE METHODS
    override fun checkEmptyLoanManagementMail(email: String): Boolean {
        return email.isNullOrEmpty()
    }

    override fun checkValidLoanManagementMail(email: String): Boolean {
        return !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun getUserLoansAndReserves(email: String){

        Log.d(TAG, "Trying to get user loans and reserves...")
        view?.disableSearchLoanButton()
        view?.showLoanManagementProgressBar()
        view?.disableAllLoanReserveButtons()

        launch{

            try{

                val pendingReserves = adminViewModel?.getUserPendingReserves(email)
                var pendingLoans = adminViewModel?.getUserPendingLoans(email)

                if(isViewAttach()){

                    view?.hideLoanManagementProgressBar()
                    view?.showLoansManagementContent()
                    if(pendingReserves!!.size > 0){
                        view?.enableInitLoanButton()
                        view?.enableCancelReserveButton()
                    }
                    if(pendingLoans!!.size > 0){
                        view?.enableEnlargeLoanButton()
                        view?.enableFinalizeLoanButton()
                    }
                    view?.initLoansManagementContent(pendingReserves, pendingLoans)

                }

                Log.d(TAG, "Succesfully getted user loans and reserves.")

            } catch (error: FirebaseGetUserReservesException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableSearchLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot get user reserves with email $email. Error -> $errorMsg.")

            } catch (error: FirebaseGetUserLoansException){

                val errorMsg = error.message.toString()

                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableSearchLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot get user loans with email $email. Error -> $errorMsg.")

            } catch (error: FirebaseGetUserReservesException) {

                val errorMsg = error.message.toString()

                if (isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableSearchLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot get user reserves with email $email. Error -> $errorMsg.")

            }

        }

    }

    override fun initLoan(reserve: Reserve) {

        val settedReserve = Reserve(
            reserve.userMail,
            reserve.resourceId,
            reserve.resourceName,
            reserve.libraryId,
            reserve.reserveDate,
            LocalDateTime.now().toString(),
            "Finalized"
        )

        val newLoan = Loan(
            reserve.userMail,
            reserve.resourceId,
            reserve.resourceName,
            reserve.libraryId,
            LocalDateTime.now().toString()
        )

        val currentUser = NeLSProject.currentUser
        var newUserReserves = currentUser.loans
        newUserReserves.remove(reserve.resourceId)
        var newUserLoans = currentUser.loans
        newUserLoans.add(reserve.resourceId)
        val settedUser = User(
            currentUser.name,
            currentUser.lastName1,
            currentUser.lastName2,
            currentUser.email,
            currentUser.nif,
            newUserReserves,
            newUserLoans,
            currentUser.favorites,
            currentUser.admin,
            currentUser.researcher,
            currentUser.avatar
        )

        launch{

            view?.showLoanManagementProgressBar()
            view?.disableAllLoanReserveButtons()

            try{
                adminViewModel?.setReserve(settedReserve)
                adminViewModel?.addLoan(newLoan)
                adminViewModel?.setUser(settedUser)
                NeLSProject.currentUser=settedUser
                if(isViewAttach()){
                    view?.hideLoanManagementProgressBar()
                    view?.showMessage("El Préstamo se ha llevado a cabo con éxito, ¡Dile que lo disfrute!.")
                    view?.navigateToAdminZone()
                }

                Log.d(TAG, "Succesfully add Loan to user ${NeLSProject.currentUser.email}.")

            } catch (error: FirebaseSetReserveException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot add Loan to user (Reserve Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            } catch (error: FirebaseAddLoanException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot add Loan to user (Loan Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            } catch (error: FirebaseSetUserException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot add Loan to user (User Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            }

        }

    }

    override fun cancelReserve(reserve: Reserve) {

        val settedReserve = Reserve(
            reserve.userMail,
            reserve.resourceId,
            reserve.resourceName,
            reserve.libraryId,
            reserve.reserveDate,
            LocalDateTime.now().toString(),
            "Cancelled"
        )

        var newResourceDisponibility = NeLSProject.currentResource!!.disponibility
        newResourceDisponibility.set(reserve.libraryId, newResourceDisponibility[reserve.libraryId]!!+1)
        var settedResource = Resource(
            NeLSProject.currentResource!!.title,
            NeLSProject.currentResource!!.author,
            NeLSProject.currentResource!!.publisher,
            NeLSProject.currentResource!!.edition,
            NeLSProject.currentResource!!.sinopsis,
            NeLSProject.currentResource!!.isbn,
            newResourceDisponibility,
            NeLSProject.currentResource!!.likes,
            NeLSProject.currentResource!!.dislikes,
            NeLSProject.currentResource!!.isOnline,
            NeLSProject.currentResource!!.urlOnline,
            NeLSProject.currentResource!!.imageUri

        )

        val currentUser = NeLSProject.currentUser
        var newUserReserves = currentUser.loans
        newUserReserves.remove(reserve.resourceId)
        val settedUser = User(
            currentUser.name,
            currentUser.lastName1,
            currentUser.lastName2,
            currentUser.email,
            currentUser.nif,
            newUserReserves,
            currentUser.loans,
            currentUser.favorites,
            currentUser.admin,
            currentUser.researcher,
            currentUser.avatar
        )

        launch{

            view?.showLoanManagementProgressBar()
            view?.disableAllLoanReserveButtons()

            try{
                adminViewModel?.setReserve(settedReserve)
                adminViewModel?.setResource(settedResource)
                adminViewModel?.setUser(settedUser)
                NeLSProject.currentUser=settedUser
                if(isViewAttach()){
                    view?.hideLoanManagementProgressBar()
                    view?.showMessage("La Reserva se ha cancelado con éxito.")
                    view?.navigateToAdminZone()
                }

                Log.d(TAG, "Succesfully add Loan to user ${NeLSProject.currentUser.email}.")

            } catch (error: FirebaseSetReserveException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot cancel Reserve to user (Reserve Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            } catch (error: FirebaseSetResourceException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot cancel Reserve to user (Resource Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            } catch (error: FirebaseSetUserException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot cancel Reserve to user (User Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            }

        }
    }

    override fun enlargeLoan(loan: Loan) {

        if(loan.status == "Enlarge"){
            if(isViewAttach()) view?.showMessage("Este préstamo ya ha sigo ampliado, no se puede volver a ampliar.")
        } else {
            val enlargedLoan = Loan(
                loan.userMail,
                loan.resourceId,
                loan.resourceName,
                loan.libraryId,
                loan.loanDate,
                loan.returnDate,
                "Enlarge",
                loan.id
            )

            launch {

                view?.showLoanManagementProgressBar()
                view?.disableAllLoanReserveButtons()

                try {

                    adminViewModel?.setLoan(enlargedLoan)

                    if (isViewAttach()) {
                        view?.hideLoanManagementProgressBar()
                        view?.showMessage("El Préstamo se ha alargado con éxito, ¡Dile que lo disfrute!.")
                        view?.navigateToAdminZone()
                    }

                    Log.d(TAG, "Succesfully enlarge Loan to user ${NeLSProject.currentUser.email}.")

                } catch (error: FirebaseSetLoanException) {

                    var errorMsg = error.message

                    if (isViewAttach()) {
                        view?.showError(errorMsg)
                        view?.hideLoanManagementProgressBar()
                        view?.enableInitLoanButton()
                        view?.enableCancelReserveButton()
                        view?.enableEnlargeLoanButton()
                        view?.enableFinalizeLoanButton()
                    }

                    Log.d(
                        TAG,
                        "ERROR: Cannot enlarge Loan to user ${NeLSProject.currentUser.email} --> $errorMsg"
                    )

                }

            }
        }

    }


    override fun finalizeLoan(loan: Loan) {

        launch{

            view?.showLoanManagementProgressBar()
            view?.disableAllLoanReserveButtons()

            try{

                var currentResource = adminViewModel?.getResource(loan.resourceId)
                var newResourceDisponibility = currentResource!!.disponibility
                newResourceDisponibility.set(loan.libraryId, newResourceDisponibility[loan.libraryId]!!+1)
                var settedResource = Resource(
                    currentResource.title,
                    currentResource.author,
                    currentResource.publisher,
                    currentResource.edition,
                    currentResource.sinopsis,
                    currentResource.isbn,
                    newResourceDisponibility,
                    currentResource.likes,
                    currentResource.dislikes,
                    currentResource.isOnline,
                    currentResource.urlOnline,
                    NeLSProject.currentResource!!.imageUri

                )

                val finalizedLoan = Loan(
                    loan.userMail,
                    loan.resourceId,
                    loan.resourceName,
                    loan.libraryId,
                    loan.loanDate,
                    LocalDateTime.now().toString(),
                    "Finalized",
                    loan.id
                )

                val currentUser = NeLSProject.currentUser
                var newUserLoans = currentUser.loans
                newUserLoans.remove(loan.resourceId)
                val settedUser = User(
                    currentUser.name,
                    currentUser.lastName1,
                    currentUser.lastName2,
                    currentUser.email,
                    currentUser.nif,
                    currentUser.reserves,
                    newUserLoans,
                    currentUser.favorites,
                    currentUser.admin,
                    currentUser.researcher,
                    currentUser.avatar
                )

                adminViewModel?.setResource(settedResource)
                adminViewModel?.setLoan(finalizedLoan)
                adminViewModel?.setUser(settedUser)
                NeLSProject.currentUser=settedUser
                if(isViewAttach()){
                    view?.hideLoanManagementProgressBar()
                    view?.showMessage("El Préstamo se ha finalizado con éxito.")
                    view?.navigateToAdminZone()
                }

                Log.d(TAG, "Succesfully finalized Loan to user ${NeLSProject.currentUser.email}.")

            }  catch (error: FirebaseSetResourceException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot finalize Loan to user (Resource Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            }  catch (error: FirebaseSetLoanException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot finalize Loan to user (Loan Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            } catch (error: FirebaseSetUserException){

                var errorMsg = error.message

                if(isViewAttach()) {
                    view?.showError(errorMsg)
                    view?.hideLoanManagementProgressBar()
                    view?.enableInitLoanButton()
                    view?.enableCancelReserveButton()
                    view?.enableEnlargeLoanButton()
                    view?.enableFinalizeLoanButton()
                }

                Log.d(TAG, "ERROR: Cannot finalize Loan to user (User Error) ${NeLSProject.currentUser.email} --> $errorMsg")

            }

        }
    }

}