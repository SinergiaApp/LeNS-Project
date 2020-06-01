package com.sinergia.eLibrary.presentation.Neurolinguistics.Model

import android.net.Uri
import com.sinergia.eLibrary.data.Model.Article

interface NeurolinguisticsViewModel {

    suspend fun getAllArticles(): ArrayList<Article>

    suspend fun getAllArticlesWithCategory(category: String): ArrayList<Article>

    suspend fun deleteArticle(article: Article)

    suspend fun downloadArticle(article: Article)

    suspend fun deleteStorageArticle(articleId: String)

}