package com.sinergia.eLibrary.presentation.UploadArticle.Model

import android.net.Uri
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsStorage

class UploadArticleViewModelImpl: UploadArticleViewModel {

    private val nelsDB = NelsDataBase()
    private val nelsStorage = NelsStorage()

    override suspend fun uploadArticle(article: Article) {
        return nelsDB.newArticle(article)
    }

    override suspend fun uploadArticleStorage(articleId: String, articleUri: Uri): Uri {
        return nelsStorage.uploadArticle(articleId, articleUri)
    }


}