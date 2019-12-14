package com.sinergia.eLibrary.presentation.Catalog.Presenter

import android.util.Log
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModelImpl

class CatalogPresenter(catalogViewModel: CatalogViewModelImpl): CatalogContract.CatalogPresenter {

    var view: CatalogContract.CatalogView ?= null
    var catalogViewModel: CatalogViewModelImpl ?= null

    init {
        this.catalogViewModel = catalogViewModel
    }

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

        view?.showPCatalogrogressBar()

        catalogViewModel?.getAllResourcesToCatalog(object: CatalogViewModel.CatalogViewModelCallBack{
            override fun onGetResourcesSuccess(resourcesList: ArrayList<Resource>) {
                view?.hideCatalogProgressBar()
                view?.initCatalog(resourcesList)
            }

            override fun onGetResourcesFailure(errorMsg: String) {
                view?.hideCatalogProgressBar()
                view?.showError(errorMsg)
            }

        })

    }
}