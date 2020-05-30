package com.sinergia.eLibrary.presentation

import android.app.Application
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User

class NeLSProject: Application() {

    companion object{
        var currentUser = User()
        var adminUser = false
        var currentLibrary: Library?= null
        var currentResource: Resource ?= null
        var currentArticle: Article?= null

        var cameraPermissionGranted = false
        var storagePermissionGranted = false

        val CAMERA_PERMISSIONS_CODE = 10
        val READ_STORAGE_PERMISSIONS_CODE = 11
        val WRITE_STORAGE_PERMISSIONS_CODE = 12
        val CAMERA_INTENT_CODE = 20
        val GALLERY_INTENT_CODE = 21

        val ARTICLE_CATEGORIES: List<String> = listOf("Conceptos Básicos", "El Lenguaje en el Cerebro", "Trastornos del Lenguaje", "Afasia")

    }


}