package com.sinergia.eLibrary.presentation.AdminZone.Model

import com.google.firebase.firestore.GeoPoint

interface AdminViewModel {

    suspend fun addNewResource(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String)

    suspend fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint)

}