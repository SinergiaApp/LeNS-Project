package com.sinergia.eLibrary.domain.UseCases

import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel

class LibraryUseCases {

    val nelsDB = NelsDataBase()

    fun getAllLibraries() {

    }

    fun addLibrary(nombre: String, direccion: String, geopoint: GeoPoint, listener: AdminViewModel.createLibrarylCallBack) {
        nelsDB.addLibrary(nombre, direccion, geopoint, listener)
    }

}