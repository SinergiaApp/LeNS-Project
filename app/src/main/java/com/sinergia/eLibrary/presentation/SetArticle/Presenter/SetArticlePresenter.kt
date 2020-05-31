package com.sinergia.eLibrary.presentation.SetArticle.Presenter

import android.util.Log
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.Exceptions.FirebaseSetArticleException
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.presentation.SetArticle.Model.SetArticleViewModel
import com.sinergia.eLibrary.presentation.SetArticle.SetArticleContract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class SetArticlePresenter(setArticleViewModel: SetArticleViewModel): SetArticleContract.SetArticlePresenter, CoroutineScope {

    val TAG = "[SET_ARTICLE_ACTIVITY]"

    var view: SetArticleContract.SetArticleView ?= null
    var setArticleViewModel: SetArticleViewModel ?= null
    val setArticlegJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + setArticlegJob

    init {
        this.setArticleViewModel = setArticleViewModel
    }

    override fun attachView(view: SetArticleContract.SetArticleView) {
        this.view = view
    }

    override fun dettachView() {
        this.view = null
    }

    override fun isViewAttach(): Boolean {
        return this.view != null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun checkEmptyArticleISSN(issn: String): Boolean {
        return issn.isNullOrEmpty()
    }

    override fun checkEmptyArticleTitle(title: String): Boolean {
        return title.isNullOrEmpty()
    }

    override fun checkEmptyArticleAuthors(authors: ArrayList<String>): Boolean {
        return authors.isNullOrEmpty()
    }

    override fun checkEmptyArticleEdition(edition: Int): Boolean {
        return edition == null
    }

    override fun checkEmptyArticleSource(source: String): Boolean {
        return source.isNullOrEmpty()
    }

    override fun checkEmptyArticleDescrption(description: String): Boolean {
        return description.isNullOrEmpty()
    }

    override fun checkEmptyFileds(
        title: String,
        authors: ArrayList<String>,
        edition: Int,
        source: String,
        description: String
    ): Boolean {
        return (
                checkEmptyArticleTitle(title) ||
                        checkEmptyArticleAuthors(authors) ||
                        checkEmptyArticleEdition(edition) ||
                        checkEmptyArticleDescrption(description)
                )
    }

    override fun setArticle(settedArticle: Article) {

        Log.d(TAG, "Trying to set Article with id ${settedArticle.id}.")

        launch {

            try{

                if(isViewAttach()){
                    view?.showSetArticleProgressBar()
                    view?.disableSetdArticleButton()
                }

                setArticleViewModel?.setArtcile(settedArticle)

                if(isViewAttach()){
                    view?.hideSetArticleProgressBar()
                    view?.enableSetArticleButton()
                    view?.showMessage(R.string.MSG_SET_ARTICLE)
                    view?.navigateToNeurolinguistics()
                }

                Log.d(TAG, "Successfully set Article with id ${settedArticle.id}.")

            } catch (error: FirebaseSetArticleException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideSetArticleProgressBar()
                    view?.enableSetArticleButton()
                    view?.showError(R.string.ERR_SET_ARTICLE)
                }

                Log.d(TAG, "ERROR: Cannot set Article to DataBase --> $errorMsg")

            }

        }


    }


}