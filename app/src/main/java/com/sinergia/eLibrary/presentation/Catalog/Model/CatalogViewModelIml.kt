package com.sinergia.eLibrary.presentation.Catalog.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases

class CatalogViewModelIml: ViewModel(), CatalogViewModel {

    var resourceUseCase = ResourcesUseCases()

    override fun getAllResourcesToCatalog(callBack: CatalogViewModel.CatalogViewModelCallBack) {
        resourceUseCase.getAllResourcesToCatalog(callBack)
    }
}