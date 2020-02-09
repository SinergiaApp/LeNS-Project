package com.sinergia.eLibrary.presentation.Catalog.Model

import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource

interface ItemCatalogViewModel {

    suspend fun getItemCatalog(isbn: String): Resource?

    suspend fun setResource(resource: Resource)

    suspend fun getAllLibraries(): ArrayList<Library>

}