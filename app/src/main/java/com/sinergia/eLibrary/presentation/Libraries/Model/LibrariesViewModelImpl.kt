package com.sinergia.eLibrary.presentation.Libraries.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.domain.UseCases.LibraryUseCases

class LibrariesViewModelImpl: ViewModel(), LibrariesViewModel {

    val librariesUseCase = LibraryUseCases()

    override suspend fun getAllLibraries(): ArrayList<Library> {
        return librariesUseCase.getAllLibraries()
    }

}