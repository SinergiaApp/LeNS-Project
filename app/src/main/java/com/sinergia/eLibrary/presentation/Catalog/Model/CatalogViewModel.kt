package com.sinergia.eLibrary.presentation.Catalog.Model

interface CatalogViewModel {

    interface CatalogViewModelCallBack {
        fun onGetResourcesSuccess()
        fun onGetResourcesFailure()
    }

    fun getAllResourcesToCatalog(callBack: CatalogViewModel.CatalogViewModelCallBack)
}