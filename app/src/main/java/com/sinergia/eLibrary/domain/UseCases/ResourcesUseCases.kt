package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.data.Model.Resource

class ResourcesUseCases {

    val nelsDB = NelsDataBase()

    suspend fun getAllResourcesToCatalog(): ArrayList<Resource>{
        return nelsDB.getAllResources()
    }

    suspend fun getResource(isbn: String): Resource?{
        return nelsDB.getResource(isbn)
    }

    suspend fun addResource(titulo: String,
                            autores: List<String>,
                            isbn: String, edicion:
                            String, editorial: String,
                            sinopsis: String,
                            disponibility: MutableMap<String, Integer>,
                            likes: MutableList<String>,
                            dislikes: MutableList<String>)
    {
        nelsDB.addResource(titulo, autores, isbn, edicion, editorial, sinopsis, disponibility, likes, dislikes)
    }

    suspend fun setResource(resource: Resource){
        nelsDB.setResource(resource)
    }

}