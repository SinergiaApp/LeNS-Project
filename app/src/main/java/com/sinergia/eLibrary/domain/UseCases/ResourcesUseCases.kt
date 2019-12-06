package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel

class ResourcesUseCases {

    val nelsDB = NelsDataBase()

    fun addResource(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String, listener: AdminViewModel.AdminViewModelCallBack){
        nelsDB.addResource(titulo, autor, iban, edicion, sinopsis, listener)
    }

}