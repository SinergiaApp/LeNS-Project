package com.sinergia.eLibrary.presentation.Catalog.Model

import com.sinergia.eLibrary.data.Model.Resource

interface ItemCatalogViewModel {

    suspend fun getItemCatalog(isbn: String): Resource?

}