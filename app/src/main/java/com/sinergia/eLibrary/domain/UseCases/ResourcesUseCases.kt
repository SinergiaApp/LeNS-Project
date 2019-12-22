package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.data.Model.Resource

class ResourcesUseCases {

    val nelsDB = NelsDataBase()

    suspend fun getAllResourcesToCatalog(): ArrayList<Resource>{
        return nelsDB.getAllResources()
    }

    suspend fun addResource(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String){
        nelsDB.addResource(titulo, autor, isbn, edicion, editorial, sinopsis)
    }

}