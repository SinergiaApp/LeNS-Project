package com.sinergia.eLibrary.presentation.UploadArticle.Model

import android.net.Uri
import com.sinergia.eLibrary.data.Model.Article

interface UploadArticleViewModel {

    suspend fun uploadArticle(article: Article)

    suspend fun uploadArticleStorage(articleId: String, articleUri: Uri): Uri

}