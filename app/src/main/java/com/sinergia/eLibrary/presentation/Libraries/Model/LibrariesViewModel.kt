package com.sinergia.eLibrary.presentation.Libraries.Model

import com.sinergia.eLibrary.data.Model.Library

interface LibrariesViewModel {

    suspend fun getAllLibraries(): ArrayList<Library>

}