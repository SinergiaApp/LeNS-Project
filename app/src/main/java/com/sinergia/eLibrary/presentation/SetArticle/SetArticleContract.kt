package com.sinergia.eLibrary.presentation.SetArticle

import com.sinergia.eLibrary.data.Model.Article

interface SetArticleContract {

    interface SetArticleView {

        fun showError(error: String)
        fun showError(error: Int)
        fun showMessage(message: String)
        fun showMessage(message: Int)
        fun showSetArticleProgressBar()
        fun hideSetArticleProgressBar()
        fun enableSetArticleButton()
        fun disableSetdArticleButton()

        fun initContent()

        fun setArticle()

        fun navigateToNeurolinguistics()

    }

    interface SetArticlePresenter{

        fun attachView(view: SetArticleView)
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

        fun setArticle(settedArticle: Article)

    }
}