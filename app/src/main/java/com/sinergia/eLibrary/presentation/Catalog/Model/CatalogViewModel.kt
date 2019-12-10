package com.sinergia.eLibrary.presentation.Catalog.Model

import com.sinergia.eLibrary.data.Model.Resource

interface CatalogViewModel {

    interface CatalogViewModelCallBack {
        fun onGetResourcesSuccess(resourcesList: ArrayList<Resource>)
        fun onGetResourcesFailure(errorMsg: String)
    }

    fun getAllResourcesToCatalog(callBack: CatalogViewModel.CatalogViewModelCallBack)
}