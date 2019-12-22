package com.sinergia.eLibrary.presentation.AdminZone.Model

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.domain.UseCases.LibraryUseCases
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases

class AdminViewModelImpl: ViewModel(), AdminViewModel {

    val resourceUseCase = ResourcesUseCases()
    val libraryUseCases = LibraryUseCases()

    //CREATE NEW RESOURCE FUNCTION
    override suspend fun addNewResource(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String) {
        resourceUseCase.addResource(titulo, autor, isbn, edicion, editorial, sinopsis)
    }

    //CREATE NEW LIBRARY FUNCTION
    override suspend fun addNewLibrary(nombre: String, direccion: String, geopoint: GeoPoint) {
        libraryUseCases.addLibrary(nombre, direccion, geopoint)
    }

}