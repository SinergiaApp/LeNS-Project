package com.sinergia.eLibrary.presentation.UploadArticle

import android.net.Uri
import com.sinergia.eLibrary.data.Model.Article

interface UploadArticleContract {

    interface UploadArticleView{

        fun showError(error: String)
        fun showError(error: Int)
        fun showMessage(message: String)
        fun showMessage(message: Int)
        fun showUploadArticleProgressBar()
        fun hideUploadArticleProgressBar()
        fun enableUploadArticleButton()
        fun disableUploadArticleButton()

        fun initCategories()

        fun uploadArticle()
        fun uploadArticleStorage()
        fun checkAndSetStoragePermissions()

        fun navigateToNeurolinguistics()

    }

    interface UploadArticlePresenter {

        fun attachView(view: UploadArticleView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun dettachJob()

        fun checkEmptyArticleISSN(issn: String): Boolean
        fun checkEmptyArticleTitle(title: String): Boolean
        fun checkEmptyArticleAuthors(authors: ArrayList<String>): Boolean
        fun checkEmptyArticleEdition(edition: Int): Boolean
        fun checkEmptyArticleSource(source: String): Boolean
        fun checkEmptyArticleDescrption(description: String): Boolean
        fun checkEmptyFileds(title: String, authors: ArrayList<String>, edition: Int, source: String, description: String): Boolean

        fun uploadArticle(article: Article, articleUri: Uri)

    }
}