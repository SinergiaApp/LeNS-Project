package com.sinergia.eLibrary.presentation.Catalog.Presenter

import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract.ItemCatalogView

class ItemCatalogPresenter: ItemCatalogContract.ItemCatalogPresenter {

    var view: ItemCatalogView ?= null


    override fun attachView(view: ItemCatalogView) {
        this.view = view
    }

    override fun dettachView() {
        view = null
    }

    override fun isViewAttached(): Boolean {
        return view != null
    }

    override fun dettachJob() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCatalog(isbn: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}