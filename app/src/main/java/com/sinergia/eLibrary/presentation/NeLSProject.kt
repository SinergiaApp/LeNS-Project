package com.sinergia.eLibrary.presentation

import android.app.Application
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User

class NeLSProject: Application() {

    companion object{
        var currentUser = User()
        var adminUser = false
        var library = "noItemSelected"
        var libraryName = "noItemSelectec"
        var currentResource: Resource ?= null
        var bookTitle = "noItemSelected"

        val CAMERA_PERMISSIONS_CODE = 1
        val CAMERA_INTENT_CODE = 2
    }


}