package com.sinergia.eLibrary.presentation.AdminZone.Model

import com.google.firebase.firestore.GeoPoint

interface AdminViewModel {

    interface createResourceCallBack{
        fun onCreateResourceSuccess()
        fun onCreateResourceFailure(errorMsg: String)
    }

    fun addNewResource(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String, listener: AdminViewModel.createResourceCallBack)

    interface createLibrarylCallBack{

        fun onCreateLibrarySuccess()
        fun onCreateLibraryFailure(errorMsg: String)

    }
    fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint, listener: AdminViewModel.createLibrarylCallBack)

}