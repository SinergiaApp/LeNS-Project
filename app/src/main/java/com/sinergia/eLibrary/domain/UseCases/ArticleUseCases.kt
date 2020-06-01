package com.sinergia.eLibrary.domain.UseCases

import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase

class ArticleUseCases {

    val nelsDB = NelsDataBase()

    suspend fun getAllArticles(): ArrayList<Article> {
        return nelsDB.getAllArticles()
    }

    suspend fun getAllArticlesWithCategory(category: String): ArrayList<Article> {
        return nelsDB.getAllArticlesWithCategory(category)
    }

    suspend fun newArticle(newArticle: Article){
        return nelsDB.newArticle(newArticle)
    }

    suspend fun setArticle(settedArticle: Article){
        return nelsDB.setArticle(settedArticle)
    }

    suspend fun deleteArticle(article: Article){
        return nelsDB.deleteArticle(article)
    }

}