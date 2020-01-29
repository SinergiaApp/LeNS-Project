package com.sinergia.eLibrary.presentation.Catalog.Presenter

import android.util.Log
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetResourceException
import com.sinergia.eLibrary.base.Exceptions.FirebaseSetResourceException
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract.ItemCatalogView
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModelImpl
import com.sinergia.eLibrary.presentation.NeLSProject
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

    override fun chekRepeatLikeDislike(list: MutableList<String>): Boolean {
        return NeLSProject.currentUser.email in list
    }

    override fun getItemCatalog(isbn: String) {

        launch {

            view?.showItemCatalogProgressBar()

            try{

                val resource = itemCatalogViewModel?.getItemCatalog(isbn)
                view?.hideItemCatalogProgressBar()
                view?.showItemCatalogContent()
                view?.initItemCatalogContent(resource)

                Log.d(TAG, "Succesfullt get ItemCatalog Resource.")

            }catch (error: FirebaseGetResourceException){

                var errorMsg = error.message
                view?.showError(errorMsg)
                view?.hideItemCatalogProgressBar()

                Log.d(TAG, "ERROR: Cannot load IemCatalog Resource from DataBase --> $errorMsg")

            }

        }


    }

    override fun setResourceLikes(resource: Resource) {

        launch{

            view?.showItemCatalogProgressBar()

            try{
                itemCatalogViewModel?.setResource(resource)
                view?.hideItemCatalogProgressBar()
                view?.showItemCatalogContent()
                view?.initItemCatalogContent(resource)
                view?.showMessage("¡Perfecto! Hemos guardado tu voto.")

            } catch (error: FirebaseSetResourceException){

                var errorMsg = error.message
                view?.showError(errorMsg)
                view?.hideItemCatalogProgressBar()


                Log.d(TAG, "ERROR: Cannot set IemCatalog Resource from DataBase --> $errorMsg")

            }

        }

    }


}