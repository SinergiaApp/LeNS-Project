package com.sinergia.eLibrary.presentation.AdminZone.Model

import android.net.Uri
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.*

interface AdminViewModel {

    suspend fun addNewResource(
        titulo: String,
        autores: List<String>,
        isbn: String,
        edicion: String,
        editorial: String,
        sinopsis: String,
        disponibility: MutableMap<String, Int>,
        likes: MutableList<String>,
        dislikes: MutableList<String>,
        isOnline: Boolean,
        urlOnline: String)

    suspend fun getResourceToModify(isbn: String): Resource?
    suspend fun setResource(resource: Resource)
    suspend fun deleteResource(deletedResource: Resource)
    suspend fun setResourceImage(resourceId: String, resourceImageURI: Uri): Uri

    suspend fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint)

    suspend fun getAllLibraries(): ArrayList<Library>

    suspend fun getLibraryToModify(id: String): Library?
    suspend fun setLibrary(library: Library)
    suspend fun deleteLibrary(deletedLibrary: Library)
    suspend fun setLibraryImage(libraryId: String, libraryImageURI: Uri): Uri

    suspend fun getUserPendingLoans(email: String): ArrayList<Loan>
    suspend fun getUserPendingReserves(email: String): ArrayList<Reserve>
    suspend fun setReserve(settedReserve: Reserve)
    suspend fun addLoan(newLoan: Loan)
    suspend fun setLoan(loan: Loan)
    suspend fun setUser(settedUser: User)

}