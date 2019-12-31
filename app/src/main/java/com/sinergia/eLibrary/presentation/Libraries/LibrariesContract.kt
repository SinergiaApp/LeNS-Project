package com.sinergia.eLibrary.presentation.Libraries

import com.sinergia.eLibrary.data.Model.Library

interface LibrariesContract {

    interface LibrariesView{

        fun showError(errorMsg: String?)
        fun showMessage(message: String?)
        fun showLibrariesProgressBar()
        fun hideLibrariesProgressBar()

        fun initLibrariesContent(librariesList: ArrayList<Library>?)
        fun navigateToLibrary(library: String)

    }

    interface LibrariesPresenter{

        fun attachView(view: LibrariesContract.LibrariesView)
        fun dettachView()
        fun isViewAttached(): Boolean
        fun dettachJob()

        fun getAllLibraries()

    }

}