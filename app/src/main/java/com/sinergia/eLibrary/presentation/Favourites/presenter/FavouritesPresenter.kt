package com.sinergia.eLibrary.presentation.Favourites.presenter

import android.util.Log
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetAllResourcesException
import com.sinergia.eLibrary.presentation.Favourites.FavouritesContract
import com.sinergia.eLibrary.presentation.Favourites.model.FavouritesViewModel
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class FavouritesPresenter(favouritesViewModel: FavouritesViewModel): FavouritesContract.FavouritesPresenter, CoroutineScope {

    val TAG = "[FAVOURITES ACTIVITY]"

    var view: FavouritesContract.FavouritesView ?= null
    var favouritesViewModel: FavouritesViewModel?= null
    val favouritesJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + favouritesJob

    init{
        this.favouritesViewModel = favouritesViewModel
    }

    override fun attachView(view: FavouritesContract.FavouritesView) {
        this.view = view
    }

    override fun dettachView() {
        this.view = null
    }

    override fun isViewAttach(): Boolean {
        return this.view != null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun getAllFavouriteResourcesToCatalog() {

        Log.d(TAG, "Trying to get Favourites Resources.")

        launch {

            try{

                if(isViewAttach()){
                    view?.eraseFavourites()
                    view?.showFavouritesProgressBar()
                }

                var favouriteResources = favouritesViewModel?.getAllFavouritesResources(NeLSProject.currentUser.email)!!

                if(isViewAttach()){
                    view?.initFavourites(favouriteResources)
                    view?.hideFavouritesgressBar()
                }

                Log.d(TAG, "Succesfully get Favourites Resources.")

            } catch (error: FirebaseGetAllResourcesException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideFavouritesgressBar()
                    view?.showError(errorMsg)
                }

                Log.d(TAG, "ERROR: Cannot load Favourites Resources from DataBase --> $errorMsg")

            }

        }

    }

    override fun getFavouriteResourceToCatalog(isbn: String) {

        Log.d(TAG, "Trying to get Favourite Resource.")

        launch {

            try{

                if(isViewAttach()){
                    view?.eraseFavourites()
                    view?.showFavouritesProgressBar()
                }

                var favouriteResource = favouritesViewModel?.getFavouriteResource(isbn)!!

                if(isViewAttach()){
                    view?.initFavourites(favouriteResource)
                    view?.hideFavouritesgressBar()
                }

                Log.d(TAG, "Succesfully get Favourite Resource.")

            } catch (error: FirebaseGetAllResourcesException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideFavouritesgressBar()
                    view?.showError(errorMsg)
                }

                Log.d(TAG, "ERROR: Cannot load Favourite Resource from DataBase --> $errorMsg")

            }

        }

    }

}