package com.sinergia.eLibrary.presentation.Neurolinguistics.Presenter

import android.util.Log
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.Exceptions.FirebaseStorageDownload
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Neurolinguistics.Model.NeurolinguisticsViewModel
import com.sinergia.eLibrary.presentation.Neurolinguistics.NeurolinguisticsContract
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ItemNeurolinguisticsPresenter(itemNeurolinguisticsViewModel: NeurolinguisticsViewModel): NeurolinguisticsContract.ItemNeurolinguisticsPresenter , CoroutineScope{

    private val TAG = "[ITEM_NEUROLINGUISTICS_ACTIVITY]"
    private val itemNeuroJob = Job()

    var view: NeurolinguisticsContract.ItemNeurolinguisticsView ?= null
    var itemNeurolinguisticsViewModel: NeurolinguisticsViewModel ?= null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + itemNeuroJob

    init{
        this.itemNeurolinguisticsViewModel = itemNeurolinguisticsViewModel
    }

    override fun attachView(view: NeurolinguisticsContract.ItemNeurolinguisticsView) {
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

    override fun downloadArticle() {

        Log.d(TAG, "Trying to download article from database.")

        launch {

            if(isViewAttach()){
                view?.showItemNeuroProgressBar()
                view?.disableButtons()
            }

            try{

                itemNeurolinguisticsViewModel?.downloadArticle(NeLSProject.currentArticle!!)

                if(isViewAttach()){
                    view?.hideItemNeuroProgressBar()
                    view?.enableButtons()
                }

                Log.d(TAG, "Succesfully download article from database.")

            } catch(error: FirebaseStorageDownload){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideItemNeuroProgressBar()
                    view?.enableButtons()
                    view?.showError(R.string.ERR_NEURO_DOWNLOAD)
                }

                Log.d(TAG, "ERROR: Cannot download article from database --> $errorMsg")

            }

        }

    }

    override fun deleteArticle() {

        Log.d(TAG, "Trying to delete article from database.")

        launch {

            if(isViewAttach()){
                view?.showItemNeuroProgressBar()
                view?.disableButtons()
            }

            try{

                itemNeurolinguisticsViewModel?.deleteStorageArticle(NeLSProject.currentArticle!!.id)
                itemNeurolinguisticsViewModel?.deleteArticle(NeLSProject.currentArticle!!)

                if(isViewAttach()){
                    view?.hideItemNeuroProgressBar()
                    view?.enableButtons()
                }

                Log.d(TAG, "Succesfully delete article from database.")

            } catch(error: FirebaseStorageDownload){

                val errorMsg = error.message

                if(isViewAttach()){
                    view?.hideItemNeuroProgressBar()
                    view?.enableButtons()
                    view?.showError(R.string.ERR_NEURO_DELETE)
                }

                Log.d(TAG, "ERROR: Cannot delete article from database --> $errorMsg")

            }

        }

    }

}