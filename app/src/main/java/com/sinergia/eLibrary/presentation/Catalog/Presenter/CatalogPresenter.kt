package com.sinergia.eLibrary.presentation.Catalog.Presenter

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetAllResourcesException
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetResourceException
import com.sinergia.eLibrary.base.Exceptions.FirebaseSetResourceException
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModelImpl
import kotlinx.coroutines.*
import java.util.*
import kotlin.collections.ArrayList
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

        Log.d(TAG, "Trying to get Catalog Resources.")

        launch {

            view?.eraseCatalog()
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

                Log.d(TAG, "ERROR: Cannot load Resources from DataBase --> $errorMsg")

            }

        }

    }

    override fun getResourceToCatalog(isbn: String) {

        launch {

            view?.eraseCatalog()
            view?.showCatalogrogressBar()

            try{

                var resource = catalogViewModel?.getResourceToCatalog(isbn)

                if(isViewAttach()){
                    view?.hideCatalogProgressBar()
                    view?.initCatalog(resource)
                }

                Log.d(TAG, "Succesfully get Catalog Resources.")

            }catch(error: FirebaseGetResourceException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideCatalogProgressBar()
                    view?.showError(errorMsg)
                }

                Log.d(TAG, "ERROR: Cannot load Resourcs from DataBase --> $errorMsg")

            }

        }

    }

    override fun setLikes(resource: Resource, usuario: String, puntuacion: Int) {

        if (puntuacion == 0){

            if(resource.dislikes.contains(usuario)){
                view?.showMessage("Ya has indicado que no te gusta este libro.")
            } else {
                resource.dislikes.add(usuario)
                if(resource.likes.contains(usuario)) resource.likes.remove(usuario)
            }

        }

        if (puntuacion == 1){

            if(resource.likes.contains(usuario)){
                view?.showMessage("Ya has indicado que te gusta este libro.")
            } else {
                resource.likes.add(usuario)
                if(resource.dislikes.contains(usuario)) resource.dislikes.remove(usuario)
            }

        }

        launch {

            try{
                catalogViewModel?.setLikes(resource)

                if(isViewAttach()){
                    view?.showMessage("¡Muchas gracias! Hemos guardado tu Valoración.")
                }

                Log.d(TAG, "Succesfully Set Resource from Database.")

            } catch (error: FirebaseSetResourceException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.showError(errorMsg)
                }

                Log.d(TAG, "ERROR: Cannot Set Resourcs from DataBase --> $errorMsg")
            }

        }


    }

}