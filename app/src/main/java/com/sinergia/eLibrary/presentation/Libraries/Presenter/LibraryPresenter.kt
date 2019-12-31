package com.sinergia.eLibrary.presentation.Libraries.Presenter

import android.util.Log
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetLibraryException
import com.sinergia.eLibrary.presentation.Libraries.LibraryContract
import com.sinergia.eLibrary.presentation.Libraries.Model.LibraryViewModelImpl
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LibraryPresenter(libraryViewModel: LibraryViewModelImpl): LibraryContract.LibraryPresenter, CoroutineScope {

    val TAG = "[LIBRARY_ACTIVITY]"

    var view: LibraryContract.LibraryView ?= null
    var libraryViewModel: LibraryViewModelImpl ?= null
    var libraryJob = Job()

    init {
        this.libraryViewModel = libraryViewModel
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + libraryJob


    override fun attachView(view: LibraryContract.LibraryView) {
        this.view = view
    }

    override fun dettachView() {
        view = null
    }

    override fun isViewAttached(): Boolean {
        return view != null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun getLibrary(isbn: String) {

        launch {

            view?.showLibraryProgressBar()

            try{

                val library = libraryViewModel?.getLibrary(NeLSProject.library)
                view?.hideLibraryProgressBar()
                view?.showLibraryContent()
                view?.initLibraryContent(library)

                Log.d(TAG, "Succesfully get LibraryActivity Resource.")

            } catch (error: FirebaseGetLibraryException){

                val errorMsg = error.message
                view?.showError(errorMsg)
                view?.hideLibraryProgressBar()

                Log.d(TAG, "ERROR: Cannot load LibraryActivity from DataBase --> $errorMsg")

            }


        }

    }
}