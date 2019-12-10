package com.sinergia.eLibrary.presentation.Catalog

interface CatalogContract {

    interface CatalogView{

        fun goToMainMenu()

        fun showError(error: String)
        fun showMessage(message: String)

        fun initCatalog()

    }

    interface CatalogPresenter{

        fun attachView(view: CatalogView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun getAllResourcesToCatalog()

    }
}