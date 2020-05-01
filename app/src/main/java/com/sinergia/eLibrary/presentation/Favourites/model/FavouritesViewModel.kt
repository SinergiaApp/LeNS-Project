package com.sinergia.eLibrary.presentation.Favourites.model

import com.sinergia.eLibrary.data.Model.Resource

interface FavouritesViewModel {

    suspend fun getAllFavouritesResources(email: String): List<Resource>
    suspend fun getFavouriteResource(isbn: String): Resource?
}