package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class ReserveUseCases {

    val nelsDB = NelsDataBase()

    suspend fun newReserve(reserve: Reserve){
        return nelsDB.newReserve(reserve)
    }

    suspend fun setReserve(reserve: Reserve){
        return nelsDB.setReserve(reserve)
    }

    suspend fun cancelReserve(cancelledReserve: String){
        return nelsDB.cancelReserve(cancelledReserve)
    }

}