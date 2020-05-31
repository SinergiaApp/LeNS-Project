package com.sinergia.eLibrary.presentation.Account.Model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.Loan
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.UseCases.FileUseCases
import com.sinergia.eLibrary.domain.UseCases.LoanUseCases
import com.sinergia.eLibrary.domain.UseCases.ReserveUseCases
import com.sinergia.eLibrary.domain.UseCases.UserUseCases

class AccountViewModelImpl: AccountViewModel, ViewModel() {

    val userUseCases = UserUseCases()
    val reserveUseCases = ReserveUseCases()
    val loanUseCases = LoanUseCases()
    val fileUseCases = FileUseCases()

    override suspend fun deleteUserForUpdate(user: User) {
        return userUseCases.deleteUser(user)
    }

    override suspend fun addUserForUpdate(user: User) {
        return userUseCases.addUser(user)
    }

    override suspend fun setUserForUpdate(settedUser: User) {
        return userUseCases.setUser(settedUser)
    }


    override suspend fun deleteAccount(user: User) {
        return userUseCases.deleteUser(user)
    }

    override suspend fun uploadImage(owner: String, imageURI: Uri): Uri {
        return fileUseCases.uploadImage(owner, imageURI)
    }

    override suspend fun getUserPendingReserves(email: String): ArrayList<Reserve> {
        return reserveUseCases.getUserPendingReserves(email)
    }

    override suspend fun getUserPendingLoans(email: String): ArrayList<Loan> {
        return loanUseCases.getUserPendingLoans(email)
    }
}