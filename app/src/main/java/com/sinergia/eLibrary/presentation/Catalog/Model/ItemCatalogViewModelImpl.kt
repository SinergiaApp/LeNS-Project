package com.sinergia.eLibrary.presentation.Catalog.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.UseCases.LibraryUseCases
import com.sinergia.eLibrary.domain.UseCases.ReserveUseCases
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases
import com.sinergia.eLibrary.domain.UseCases.UserUseCases

class ItemCatalogViewModelImpl: ViewModel(), ItemCatalogViewModel {

    private val resourceUseCases = ResourcesUseCases()
    private val libraryUseCases = LibraryUseCases()
    private val reserveUseCases = ReserveUseCases()
    private val userUserCases = UserUseCases()

    override suspend fun getItemCatalog(isbn: String): Resource? {
        return resourceUseCases.getResource(isbn)
    }

    override suspend fun setResource(resource: Resource) {
        return resourceUseCases.setResource(resource)
    }

    override suspend fun getAllLibraries(): ArrayList<Library> {
        return libraryUseCases.getAllLibraries()
    }

    override suspend fun setUser(settedUser: User) {
        return userUserCases.setUser(settedUser)
    }

    override suspend fun newReserve(reserve: Reserve) {
        return reserveUseCases.newReserve(reserve)
    }

    override suspend fun cancelReserve(cancelledReserve: String) {
        return reserveUseCases.cancelReserve(cancelledReserve)
    }

}