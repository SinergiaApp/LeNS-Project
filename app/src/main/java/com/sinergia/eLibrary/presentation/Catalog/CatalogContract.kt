package com.sinergia.eLibrary.presentation.Catalog

import android.content.Context
import com.sinergia.eLibrary.data.Model.Resource

interface CatalogContract {

    interface CatalogView{

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showCatalogrogressBar()
        fun hideCatalogProgressBar()

        fun initCatalog(resourcesList: ArrayList<Resource>?)
        fun initCatalog(book: Resource?)
        fun navigateToBook(resource: Resource)
        fun eraseCatalog()

        fun startScan()
        fun checkAndSetCamentaPermissions()

    }

    interface CatalogPresenter{

        fun attachView(view: CatalogView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun dettachJob()
        fun getAllResourcesToCatalog()
        fun getResourceToCatalog(isbn: String)

        fun setLikes(resource: Resource, usuario: String, puntuacion: Int)

    }
}