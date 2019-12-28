package com.sinergia.eLibrary.presentation.Catalog.Presenter

import android.util.Log
import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetAllResourcesException
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModelImpl
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CatalogPresenter(catalogViewModel: CatalogViewModelImpl): CatalogContract.CatalogPresenter, CoroutineScope {

    val TAG = "[CATALOG_ACTIVITY]"

    var view: CatalogContract.CatalogView ?= null
    var catalogViewModel: CatalogViewModelImpl ?= null
    val catalogJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + catalogJob

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

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun getAllResourcesToCatalog() {

        launch {

            view?.showCatalogrogressBar()

            try{

                var resourcesList = catalogViewModel?.getAllResourcesToCatalog()

                if(isViewAttach()){
                    view?.hideCatalogProgressBar()
                    view?.initCatalog(resourcesList)
                }

                Log.d(TAG, "Succesfully get Catalog Resources.")

            } catch(error: FirebaseGetAllResourcesException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideCatalogProgressBar()
                    view?.showError(errorMsg)
                }

                Log.d(TAG, "ERROR: Cannot load Resourcs from DataBase --> $errorMsg")

            }

        }

    }
}