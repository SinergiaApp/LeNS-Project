package com.sinergia.eLibrary.presentation.Neurolinguistics.Presenter

import android.util.Log
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetAllArticlesException
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.presentation.Neurolinguistics.Model.NeurolinguisticsViewModel
import com.sinergia.eLibrary.presentation.Neurolinguistics.NeurolinguisticsContract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class NeurolinguisticsPresenter(neurolinguisticsViewModel: NeurolinguisticsViewModel): NeurolinguisticsContract.NeurolinguisticsPresenter, CoroutineScope {

    private val TAG = "[NEUROLINGUISTICS_ACTIVITY]"
    private val neuroJob = Job()

    var view: NeurolinguisticsContract.NeurolinguisticsView ?= null
    var neurolinguisticsViewModel: NeurolinguisticsViewModel ?= null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + neuroJob

    init{
        this.neurolinguisticsViewModel = neurolinguisticsViewModel
    }

    override fun attachView(view: NeurolinguisticsContract.NeurolinguisticsView) {
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

    override fun getAllArticlesToCatalog() {

        Log.d(TAG, "Trying to get Articles Resources.")

        launch {

            if(isViewAttach()){
                view?.eraseCatalog()
                view?.showNeuroProgressBar()
                view?.disableUploadButton()
                view?.hideContent()
            }

            try{

                var articlesList = neurolinguisticsViewModel?.getAllArticles()

                if(isViewAttach()){
                    view?.hideNeuroProgressBar()
                    view?.initCatalog(articlesList)
                    view?.enableUploadButton()
                    view?.showContent()
                }

                Log.d(TAG, "Succesfully get Articles Resources.")

            } catch(error: FirebaseGetAllArticlesException){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideNeuroProgressBar()
                    view?.showError(R.string.ERR_NEURO_GETALL)
                    view?.showContent()
                }

                Log.d(TAG, "ERROR: Cannot load Articles from DataBase --> $errorMsg")

            }

        }

    }

    override fun search(articlesList: ArrayList<Article>, searcher: String) {

        if(isViewAttach()){
            view?.hideContent()
        }

        var searchResult = arrayListOf<Article>()
        var stringPattern = ""

        for(word in searcher.split(" ")){
            stringPattern += "(${word.toLowerCase()})|"
        }
        stringPattern = stringPattern.substring(0, stringPattern.length-1)

        val pattern = stringPattern.toRegex()

        for(article in articlesList){
            if(pattern.containsMatchIn(article.title.toLowerCase()  )) searchResult.add(article)
        }

        if(isViewAttach()) {
            if(searchResult.size == 0){
                view?.showError(R.string.ERR_SEARCH)
                view?.initCatalog(articlesList)
                view?.showContent()
            } else {
                view?.initCatalog(searchResult)
                view?.showContent()
            }
        }

    }
}