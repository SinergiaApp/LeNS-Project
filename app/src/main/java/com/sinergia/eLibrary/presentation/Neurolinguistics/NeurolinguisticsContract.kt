package com.sinergia.eLibrary.presentation.Neurolinguistics

import com.sinergia.eLibrary.data.Model.Article

interface NeurolinguisticsContract {

    interface NeurolinguisticsView{

        fun showError(error: String)
        fun showError(error: Int)
        fun showMessage(message: String)
        fun showMessage(message: Int)
        fun showNeuroProgressBar()
        fun hideNeuroProgressBar()
        fun showUploadButton()
        fun showContent()
        fun hideContent()
        fun enableUploadButton()
        fun disableUploadButton()

        fun initCatalog(articlesList: ArrayList<Article>?)
        fun navigateToArticle(article: Article)
        fun eraseCatalog()

        fun search()
        fun uploadArticle()

        fun navigateToNeurolinguistics()

    }

    interface ItemNeurolinguisticsView{

        fun showError(error: String)
        fun showError(error: Int)
        fun showMessage(message: String)
        fun showMessage(message: Int)
        fun showItemNeuroProgressBar()
        fun hideItemNeuroProgressBar()
        fun showUpdateAndDeleteButtons()
        fun enableButtons()
        fun disableButtons()

        fun initContent()

        fun downloadArticle()
        fun setArticle()
        fun deleteArticle()

        fun navigateToNeurolinguistics()

    }

    interface NeurolinguisticsPresenter{

        fun attachView(view: NeurolinguisticsView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun dettachJob()
        fun getAllArticlesToCatalog(category: String)

        fun search(articlesList: ArrayList<Article>, searcher: String)

    }

    interface ItemNeurolinguisticsPresenter{

        fun attachView(view: ItemNeurolinguisticsView)
        fun dettachView()
        fun isViewAttach(): Boolean
        fun dettachJob()

        fun downloadArticle()
        fun deleteArticle()

    }

}