package com.sinergia.eLibrary.presentation

import android.app.Application
import com.sinergia.eLibrary.data.Model.User

class NeLSProject: Application() {

    companion object{
        var currentUser = User()
        var adminUser = false
        var library = "noItemSelected"
        var book = "noItemSelected"
    }


}