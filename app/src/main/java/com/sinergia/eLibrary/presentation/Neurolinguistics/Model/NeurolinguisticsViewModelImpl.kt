package com.sinergia.eLibrary.presentation.Neurolinguistics.Model

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.domain.UseCases.ArticleUseCases
import com.sinergia.eLibrary.domain.UseCases.FileUseCases

class NeurolinguisticsViewModelImpl: ViewModel(), NeurolinguisticsViewModel {

    val articleUseCases = ArticleUseCases()
    val fileuseCases = FileUseCases()

    override suspend fun getAllArticles(): ArrayList<Article> {
        return articleUseCases.getAllArticles()
    }

    override suspend fun deleteArticle(article: Article) {
        return articleUseCases.deleteArticle(article)
    }

    override suspend fun downloadArticle(article: Article) {
        return fileuseCases.downloadArticle(article)
    }

    override suspend fun deleteStorageArticle(articleId: String) {
        return fileuseCases.deleteArticle(articleId)
    }


}