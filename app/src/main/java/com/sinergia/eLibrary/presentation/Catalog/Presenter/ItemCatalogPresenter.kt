package com.sinergia.eLibrary.presentation.Catalog.Presenter

import android.util.Log
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetResourceException
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract.ItemCatalogView
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModelImpl
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ItemCatalogPresenter(itemCatalogViewModel: ItemCatalogViewModelImpl): ItemCatalogContract.ItemCatalogPresenter, CoroutineScope {

    val TAG = "[ITEM_CAT_ACTIVITY]"

    var view: ItemCatalogView ?= null
    var itemCatalogViewModel: ItemCatalogViewModel ?= null
    val itemCatalogJob = Job()

    init{
        this.itemCatalogViewModel = itemCatalogViewModel
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + itemCatalogJob



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
        coroutineContext.cancel()
    }

    override fun getItemCatalog(isbn: String) {

        launch {

            view?.showItemCatalogProgressBar()

            try{

                val resource = itemCatalogViewModel?.getItemCatalog(isbn)
                view?.hideItemCatalogProgressBar()
                view?.showItemCatalogContent()
                view?.initItemCatalogContent(resource)

                if(resource?.isOnline!!){
                    view?.enableOnLineButton(resource.urlOnline)
                } else {
                    view?.disableOnLineButton()
                }

                view?.disableDisponibilityButtom()
                for(disponibility in resource.disponibility.values){
                    if(disponibility < 0){
                        view?.enableDisponibilityButtom()

                        break
                    }
                }

                Log.d(TAG, "Succesfullt get ItemCatalog Resource.")

            }catch (error: FirebaseGetResourceException){


                var errorMsg = error.message
                view?.showError(errorMsg)
                view?.hideItemCatalogProgressBar()

                Log.d(TAG, "ERROR: Cannot load IemCatalog Resource from DataBase --> $errorMsg")

            }

        }


    }
}