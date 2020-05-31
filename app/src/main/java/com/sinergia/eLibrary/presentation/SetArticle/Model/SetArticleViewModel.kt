package com.sinergia.eLibrary.presentation.SetArticle.Model

import com.sinergia.eLibrary.data.Model.Article

interface SetArticleViewModel {

    suspend fun setArtcile(settedArticle: Article)

}