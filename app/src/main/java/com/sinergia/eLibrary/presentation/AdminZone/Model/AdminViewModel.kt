package com.sinergia.eLibrary.presentation.AdminZone.Model

import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.Library

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
        dislikes: MutableList<String>)

    suspend fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint)

    suspend fun getAllLibraries(): ArrayList<Library>

}