package com.sinergia.eLibrary.presentation.Catalog

import com.sinergia.eLibrary.data.Model.Resource

interface CatalogContract {

    interface CatalogView{

        fun goToMainMenu()

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showCatalogrogressBar()
        fun hideCatalogProgressBar()

        fun initCatalog(resourcesList: ArrayList<Resource>?)
        fun initCatalog(resource: Resource?)
        fun navigateToBook(isbn: String)
        fun eraseCatalog()

    }

    interface CatalogPresenter{

        fun attachView(view: CatalogView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun dettachJob()
        fun getAllResourcesToCatalog()
        fun getResourceToCatalog(isbn: String)
        fun initScan()

        fun setLikes(resource: Resource, usuario: String, puntuacion: Int)

    }
}