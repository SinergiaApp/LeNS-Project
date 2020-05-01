package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.data.Model.Resource

class ResourcesUseCases {

    val nelsDB = NelsDataBase()

    suspend fun getAllResourcesToCatalog(): ArrayList<Resource>{
        return nelsDB.getAllResources()
    }

    suspend fun getFavouriteResources(email: String): List<Resource>{
        return nelsDB.getFavouriteResources(email)
    }

    suspend fun getResource(isbn: String): Resource?{
        return nelsDB.getResource(isbn)
    }

    suspend fun getFavouriteResource(isbn: String): Resource{
        return nelsDB.getFavouriteResource(isbn)
    }

    suspend fun addResource(titulo: String,
                            autores: List<String>,
                            isbn: String, edicion:
                            String, editorial: String,
                            sinopsis: String,
                            disponibility: MutableMap<String, Integer>,
                            likes: MutableList<String>,
                            dislikes: MutableList<String>,
                            isOnline: Boolean,
                            urlOnline: String)
    {
        nelsDB.addResource(titulo, autores, isbn, edicion, editorial, sinopsis, disponibility, likes, dislikes, isOnline, urlOnline)
    }

    suspend fun setResource(resource: Resource){
        nelsDB.setResource(resource)
    }

}