package com.sinergia.eLibrary.presentation.MainPage.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity

class MainPage : BaseActivity() {

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.activity_main_page
    }
}

