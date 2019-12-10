package com.sinergia.eLibrary.presentation.Catalog.Presenter

import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModelIml

class CatalogPresenter(catalogViewModel: CatalogViewModelIml): CatalogContract.CatalogPresenter {

    var view: CatalogContract.CatalogView ?= null
    var catalogViewModel: CatalogViewModelIml ?= null

    override fun attachView(view: CatalogContract.CatalogView) {
        this.view = view
    }

    override fun dettachView() {
        this.view = null
    }

    override fun isViewAttach(): Boolean {
        return view != null
    }

    override fun getAllResourcesToCatalog() {

        catalogViewModel?.getAllResourcesToCatalog(object: CatalogViewModel.CatalogViewModelCallBack{
            override fun onGetResourcesSuccess() {
                view?.initCatalog()
            }

            override fun onGetResourcesFailure() {

            }

        })

    }
}