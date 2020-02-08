package com.sinergia.eLibrary.presentation.AdminZone.Model

import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource

interface AdminViewModel {

    suspend fun addNewResource(
        titulo: String,
        autores: List<String>,
        isbn: String,
        edicion: String,
        editorial: String,
        sinopsis: String,
        disponibility: MutableMap<String, Integer>,
        likes: MutableList<String>,
        dislikes: MutableList<String>,
        isOnline: Boolean,
        urlOnline: String)

    suspend fun getResourceToModify(isbn: String): Resource?
    suspend fun setResource(resource: Resource)

    suspend fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint)

    suspend fun getAllLibraries(): ArrayList<Library>

    suspend fun getLibraryToModify(id: String): Library?
    suspend fun setLibrary(library: Library)

}