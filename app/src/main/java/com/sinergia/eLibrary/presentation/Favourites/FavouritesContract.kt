package com.sinergia.eLibrary.presentation.Favourites

import android.content.Context
import com.sinergia.eLibrary.data.Model.Resource

interface FavouritesContract {

    interface FavouritesView{

        fun showError(error: String?)
        fun showMessage(message: String)
        fun showFavouritesProgressBar()
        fun hideFavouritesgressBar()

        fun initFavourites(resourcesList: List<Resource>)
        fun initFavourites(book: Resource)
        fun navigateToBook(resource: Resource)
        fun eraseFavourites()

        fun startScan()
        fun checkAndSetCamentaPermissions()

    }

    interface FavouritesPresenter {

        fun attachView(view: FavouritesContract.FavouritesView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun dettachJob()
        fun getAllFavouriteResourcesToCatalog()
        fun getFavouriteResourceToCatalog(isbn: String)

    }

}