package com.sinergia.eLibrary.presentation.Catalog.Model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases

class CatalogViewModelImpl: ViewModel(), CatalogViewModel {

    var resourceUseCase = ResourcesUseCases()

    override fun getAllResourcesToCatalog(callBack: CatalogViewModel.CatalogViewModelCallBack) {
        resourceUseCase.getAllResourcesToCatalog(callBack)
    }
}