package com.sinergia.eLibrary.domain.UseCases

import com.google.firebase.firestore.FirebaseFirestore
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class UserUseCase {

    val nelsDB = NelsDataBase()

    fun addUserDB(nombre: String, apellidos: String, email: String, contraseña: String, admin: Boolean){
        nelsDB.addUser(nombre, apellidos, email, contraseña, admin)
    }

}