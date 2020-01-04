package com.sinergia.eLibrary.presentation.Catalog

import com.sinergia.eLibrary.data.Model.Resource


interface ItemCatalogContract {

    interface ItemCatalogView {

        fun showMessage(message: String?)
        fun showError(errorMsg: String?)
        fun showItemCatalogProgressBar()
        fun hideItemCatalogProgressBar()
        fun showItemCatalogContent()
        fun hideItemCatalogContent()

        fun initItemCatalogContent(resource: Resource?)

    }

    interface ItemCatalogPresenter {

        fun attachView(view: ItemCatalogView)
        fun dettachView()
        fun isViewAttached(): Boolean
        fun dettachJob()

        fun getItemCatalog(isbn: String)

    }

}