package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class UserUseCases {

    val nelsDB = NelsDataBase()

    suspend fun addUser(newUser: User){
        nelsDB.addUser(newUser)
    }

    suspend fun getUser(email: String): User{
        return nelsDB.getUser(email)
    }

    suspend fun setUser(settedUser: User){
        return nelsDB.setUser(settedUser)
    }

    suspend fun deleteUser(user: User){
        return nelsDB.deleteUser(user)
    }

}