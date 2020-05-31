package com.sinergia.eLibrary.presentation.SetArticle.Model

import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class SetArticleViewModelImpl: SetArticleViewModel {

    val nelsDB = NelsDataBase()

    override suspend fun setArtcile(settedArticle: Article) {
        return nelsDB.setArticle(settedArticle)
    }
    
}