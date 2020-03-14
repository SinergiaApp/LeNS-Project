package com.sinergia.eLibrary.presentation.Catalog.Model

import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User

interface ItemCatalogViewModel {

    suspend fun getItemCatalog(isbn: String): Resource?

    suspend fun setResource(resource: Resource)

    suspend fun getAllLibraries(): ArrayList<Library>

    suspend fun setUser(settedUser: User)
    suspend fun newReserve(reserve: Reserve)
    suspend fun cancelReserve(cancelledReserve: String)


}