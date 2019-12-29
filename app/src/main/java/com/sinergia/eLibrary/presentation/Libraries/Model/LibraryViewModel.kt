package com.sinergia.eLibrary.presentation.Libraries.Model

import com.sinergia.eLibrary.data.Model.Library

interface LibraryViewModel {

    suspend fun getLibrary(id: String): Library

}