package com.sinergia.eLibrary.presentation.Catalog.Model

import com.sinergia.eLibrary.data.Model.Resource

interface CatalogViewModel {

    suspend fun getAllResourcesToCatalog(): ArrayList<Resource>
}