package com.sinergia.eLibrary.presentation.Catalog

import android.content.Context
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

        fun chekCameraPermissions(context: Context):Boolean

        fun setLikes(resource: Resource, usuario: String, puntuacion: Int)

    }
}