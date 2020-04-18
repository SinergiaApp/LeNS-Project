package com.sinergia.eLibrary.presentation.AdminZone.Model

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.*
import com.sinergia.eLibrary.domain.UseCases.*

class AdminViewModelImpl: ViewModel(), AdminViewModel {

    val resourceUseCase = ResourcesUseCases()
    val libraryUseCases = LibraryUseCases()
    val reserveUseCases = ReserveUseCases()
    val loanUseCases = LoanUseCases()
    val userUseCases = UserUseCases()

    // RESOURCES METHODS
    override suspend fun addNewResource(
        titulo: String,
        autores: List<String>,
        isbn: String,
        edicion: String,
        editorial: String,
        sinopsis: String,
        disponibility: MutableMap<String, Integer>,
        likes: MutableList<String>,
        dislikes: MutableList<String>,
        isOnline: Boolean,
        urlOnline: String)
    {
        resourceUseCase.addResource(titulo, autores, isbn, edicion, editorial, sinopsis, disponibility, likes, dislikes, isOnline, urlOnline)
    }


    override suspend fun getResourceToModify(isbn: String): Resource? {
        return resourceUseCase.getResource(isbn)
    }
    override suspend fun setResource(resource: Resource) {
        resourceUseCase.setResource(resource)
    }

    override suspend fun getAllLibraries(): ArrayList<Library> {
        return libraryUseCases.getAllLibraries()
    }

    // LIBRARIES METHODS
    override suspend fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint) {
        libraryUseCases.addLibrary(nombre, direccion, geopoint)
    }

    override suspend fun getLibraryToModify(id: String): Library? {
        return libraryUseCases.getLibrary(id)
    }

    override suspend fun setLibrary(library: Library) {
        libraryUseCases.setLibrary(library)
    }

    // RESERVE AND LOAN METHODS
    override suspend fun getUserPendingLoans(email: String): ArrayList<Loan> {
        return loanUseCases.getUserPendingLoans(email)
    }

    override suspend fun getUserPendingReserves(email: String): ArrayList<Reserve> {
        return reserveUseCases.getUserPendingReserves(email)
    }

    override suspend fun setReserve(settedReserve: Reserve) {
        return reserveUseCases.setReserve(settedReserve)
    }

    override suspend fun addLoan(newLoan: Loan) {
        return loanUseCases.newLoan(newLoan)
    }

    override suspend fun setLoan(loan: Loan) {
        return loanUseCases.setLoan(loan)
    }

    override suspend fun setUser(settedUser: User) {
        return userUseCases.setUser(settedUser)
    }

}