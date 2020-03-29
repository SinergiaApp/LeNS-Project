package com.sinergia.eLibrary.presentation

import android.app.Application
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User

class NeLSProject: Application() {

    companion object{
        var currentUser = User()
        var adminUser = false
        var currentLibrary: Library?= null
        var currentResource: Resource ?= null

        val CAMERA_PERMISSIONS_CODE = 1
        val CAMERA_INTENT_CODE = 2
    }


}