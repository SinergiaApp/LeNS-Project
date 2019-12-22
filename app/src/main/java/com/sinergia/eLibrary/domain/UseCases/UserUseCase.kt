package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class UserUseCase {

    val nelsDB = NelsDataBase()

    suspend fun addUserDB(nombre: String, apellidos: String, email: String, admin: Boolean, resources: Map<String, String>){
        nelsDB.addUser(nombre, apellidos, email, admin, resources)
    }

}