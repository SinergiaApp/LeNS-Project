package com.sinergia.eLibrary.presentation.AdminZone.Model

import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.domain.UseCases.ResourcesUseCases

class AdminViewModelImpl: ViewModel(), AdminViewModel {

    val resourceUseCase = ResourcesUseCases()

    override fun addNewResource(titulo: String, autor: String, isbn: String, edicion: String, sinopsis: String, listener: AdminViewModel.AdminViewModelCallBack) {
        resourceUseCase.addResource(titulo, autor, isbn, edicion, sinopsis, listener)
    }

}