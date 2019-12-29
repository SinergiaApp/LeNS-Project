package com.sinergia.eLibrary.presentation.Libraries.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.domain.UseCases.LibraryUseCases

class  LibraryViewModelImpl: ViewModel(), LibraryViewModel {

    val libraryUseCase = LibraryUseCases()

    override suspend fun getLibrary(id: String): Library {
            return libraryUseCase.getLibrary(id)
        }

}