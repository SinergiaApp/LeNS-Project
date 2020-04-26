package com.sinergia.eLibrary.presentation.Favourites

import android.content.Context
import com.sinergia.eLibrary.data.Model.Resource

interface FavouritesContract {

    interface favouritesView{

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showFavouritesgressBar()
        fun hideFavouritesgressBar()

        fun initFavourites(resourcesList: ArrayList<Resource>?)
        fun initFavourites(book: Resource?)
        fun navigateToBook(resource: Resource)
        fun eraseCatalog()

        fun startScan()
        fun checkAndSetCamentaPermissions()

    }

    interface favouritesPresenter {

        fun attachView(view: FavouritesContract.favouritesView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun dettachJob()
        fun getAllFavouriteResourcesToCatalog()
        fun getFavouriteResourceToCatalog(isbn: String)

        fun chekCameraPermissions(context: Context):Boolean

    }

}