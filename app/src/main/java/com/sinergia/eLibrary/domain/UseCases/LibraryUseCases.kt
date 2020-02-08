package com.sinergia.eLibrary.domain.UseCases

import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class LibraryUseCases {

    val nelsDB = NelsDataBase()

    suspend fun getAllLibraries(): ArrayList<Library> {
        return nelsDB.getAllLibraries()
    }

    suspend fun getLibrary(id: String): Library{
        return nelsDB.getLibrary(id)
    }

    suspend fun addLibrary(nombre: String, direccion: String, geopoint: GeoPoint) {
        nelsDB.addLibrary(nombre, direccion, geopoint)
    }

    suspend fun setLibrary(library: Library){
        nelsDB.setLibrary(library)
    }

}