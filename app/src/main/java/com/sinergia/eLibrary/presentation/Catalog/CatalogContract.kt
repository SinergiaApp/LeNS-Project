package com.sinergia.eLibrary.presentation.Catalog

import com.sinergia.eLibrary.data.Model.Resource

interface CatalogContract {

    interface CatalogView{

        fun goToMainMenu()

        fun showError(error: String)
        fun showMessage(message: String)
        fun showPCatalogrogressBar()
        fun hideCatalogProgressBar()

        fun initCatalog(resourcesList: ArrayList<Resource>)

    }

    interface CatalogPresenter{

        fun attachView(view: CatalogView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun getAllResourcesToCatalog()

    }
}