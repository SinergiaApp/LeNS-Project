package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.Model.Loan
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class LoanUseCases {

    val nelsDB = NelsDataBase()

    suspend fun getUserPendingLoans(email: String): ArrayList<Loan>{
        return nelsDB.getUserPendingLoans(email)
    }

    suspend fun newLoan(loan: Loan){
        return nelsDB.newLoan(loan)
    }

    suspend fun setLoan(loan: Loan){
        return nelsDB.setLoan(loan)
    }

    suspend fun finalizeLoan(cancelledLoan: String){
        return nelsDB.finalizeLoan(cancelledLoan)
    }

}