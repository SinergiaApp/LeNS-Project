package com.sinergia.eLibrary.presentation.Libraries.Presenter

import android.util.Log
import com.sinergia.eLibrary.base.Exceptions.FirebaseCreateLibraryException
import com.sinergia.eLibrary.presentation.Libraries.LibrariesContract
import com.sinergia.eLibrary.presentation.Libraries.Model.LibrariesViewModelImpl
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class LibrariesPresenter(librariesViewModel: LibrariesViewModelImpl): LibrariesContract.LibrariesPresenter, CoroutineScope {

    val TAG = "[LIBRARIES_ACTIVITY]"

    var view: LibrariesContract.LibrariesView? = null
    var librariesViewModel: LibrariesViewModelImpl ?= null
    var librariesJob = Job()

    init {
        this.librariesViewModel = librariesViewModel
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + librariesJob


    override fun attachView(view: LibrariesContract.LibrariesView) {
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

    override fun getAllLibraries() {

        launch{

            view?.showLibrariesProgressBar()

            try {

                var libraiesList = librariesViewModel?.getAllLibraries()

                if(isViewAttached()){
                    view?.hideLibrariesProgressBar()
                    view?.initLibrariesContent(libraiesList)
                }

                Log.d(TAG, "Succesfully get Libraries Resources.")

            }catch (error: FirebaseCreateLibraryException){

                val errorMsg= error.message

                if(isViewAttached()){
                    view?.hideLibrariesProgressBar()
                    view?.showError(errorMsg)
                }

                Log.d(TAG, "ERROR: Cannot load Libraries from DataBase --> $errorMsg")

            }



        }

    }
}