package com.sinergia.eLibrary.presentation.UploadArticle.Presenter

import android.net.Uri
import android.util.Log
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.Exceptions.FirebaseAddArticleException
import com.sinergia.eLibrary.base.Exceptions.FirebaseStorageUploadArticleException
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.presentation.UploadArticle.Model.UploadArticleViewModel
import com.sinergia.eLibrary.presentation.UploadArticle.UploadArticleContract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class UploadArticlePresenter(uploadArticleViewModel: UploadArticleViewModel): UploadArticleContract.UploadArticlePresenter, CoroutineScope {

    val TAG = "[UPLOAD_ARTICLE_ACTIVITY]"

    var view: UploadArticleContract.UploadArticleView ?= null
    var uploadArticleViewModel: UploadArticleViewModel ?= null
    val uploadArticlegJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + uploadArticlegJob

    init {
        this.uploadArticleViewModel = uploadArticleViewModel
    }

    override fun attachView(view: UploadArticleContract.UploadArticleView) {
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
        return authors.size == 0
    }

    override fun checkEmptyArticleEdition(edition: Int): Boolean {
        return edition == -1
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

    override fun uploadArticle(article: Article, articleUri: Uri) {

        Log.d(TAG, "Trying to upload new Article.")

        launch{

            try{

                if(isViewAttach()){
                    view?.showUploadArticleProgressBar()
                    view?.disableUploadArticleButton()
                }


                var articleUri = uploadArticleViewModel?.uploadArticleStorage(article.id, articleUri).toString()
                article.downloadURI = articleUri
                uploadArticleViewModel?.uploadArticle(article)

                if(isViewAttach()){
                    view?.hideUploadArticleProgressBar()
                    view?.enableUploadArticleButton()
                    view?.showMessage(R.string.MSG_UPLOAD_ARTICLE)
                    view?.navigateToNeurolinguistics()
                }

                Log.d(TAG, "Successfully upload new Article.")

            } catch (error: FirebaseAddArticleException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideUploadArticleProgressBar()
                    view?.enableUploadArticleButton()
                    view?.showError(R.string.ERR_UPLOAD_ARTICLE)
                }

                Log.d(TAG, "ERROR: Cannot upload Article to DataBase --> $errorMsg")

            } catch (error: FirebaseStorageUploadArticleException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideUploadArticleProgressBar()
                    view?.enableUploadArticleButton()
                    view?.showError(R.string.ERR_UPLOAD_ARTICLE)
                }

                Log.d(TAG, "ERROR: Cannot upload Article to DataBase --> $errorMsg")

            }


        }

    }
}