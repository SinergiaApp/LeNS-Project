package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.Model.Loan
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class LoanUseCases {

    val nelsDB = NelsDataBase()

    suspend fun newLoan(loan: Loan){
        return nelsDB.newLoan(loan)
    }

    suspend fun setLoan(loan: Loan){
        return nelsDB.setLoan(loan)
    }

    suspend fun cancelLoan(cancelledLoan: String){
        return nelsDB.cancelLoan(cancelledLoan)
    }

}