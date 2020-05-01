package com.sinergia.eLibrary.presentation.Favourites.model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases

class FavouritesViewModelImpl: ViewModel(), FavouritesViewModel {

    val resouceUseCases = ResourcesUseCases()

    override suspend fun getAllFavouritesResources(email: String): List<Resource> {
        return resouceUseCases.getFavouriteResources(email)
    }

    override suspend fun getFavouriteResource(isbn: String): Resource? {
        return resouceUseCases.getFavouriteResource(isbn)
    }
}