package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel

class ResourcesUseCases {

    val nelsDB = NelsDataBase()

    fun getAllResourcesToCatalog(callBack: CatalogViewModel.CatalogViewModelCallBack){
        nelsDB.getAllResources(callBack)
    }

    fun addResource(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String, listener: AdminViewModel.createResourceCallBack){
        nelsDB.addResource(titulo, autor, isbn, edicion, editorial, sinopsis, listener)
    }

}