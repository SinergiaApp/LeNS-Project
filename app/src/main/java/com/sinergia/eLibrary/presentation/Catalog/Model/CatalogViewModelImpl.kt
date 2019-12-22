package com.sinergia.eLibrary.presentation.Catalog.Model


import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases
import com.sinergia.eLibrary.data.Model.Resource

class CatalogViewModelImpl: ViewModel(), CatalogViewModel {

    var resourceUseCase = ResourcesUseCases()

    override suspend fun getAllResourcesToCatalog(): ArrayList<Resource> {
        return resourceUseCase.getAllResourcesToCatalog()
    }
}