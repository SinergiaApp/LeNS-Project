package com.sinergia.eLibrary.presentation.Libraries

import com.sinergia.eLibrary.data.Model.Library

interface LibraryContract {

    interface LibraryView{

        fun showMessage(message: String?)
        fun showError(errorMsg: String?)
        fun showLibraryProgressBar()
        fun hideLibraryProgressBar()
        fun showLibraryContent()
        fun hideLibraryContent()

        fun initLibraryContent(library: Library?)

    }

    interface LibraryPresenter{

        fun attachView(view: LibraryView)
        fun dettachView()
        fun isViewAttached(): Boolean
        fun dettachJob()

        fun getLibrary(isbn: String)

    }

}