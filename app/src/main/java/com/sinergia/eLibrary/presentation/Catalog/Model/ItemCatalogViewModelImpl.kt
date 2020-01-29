package com.sinergia.eLibrary.presentation.Catalog.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases

class ItemCatalogViewModelImpl: ViewModel(), ItemCatalogViewModel {

    private val resourceUseCases = ResourcesUseCases()

    override suspend fun getItemCatalog(isbn: String): Resource? {
        return resourceUseCases.getResource(isbn)
    }

    override suspend fun setResource(resource: Resource) {
        return resourceUseCases.setResource(resource)
    }

}