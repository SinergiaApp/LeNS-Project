package com.sinergia.eLibrary.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.NeLSProject


abstract class BaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
        )

        super.onCreate(savedInstanceState)
        setContentView(getLayout())
        NeLSProject.backButtonPressedTwice = false
    }

    override fun onBackPressed() {
        backButton()
    }

    @LayoutRes
    abstract fun getLayout(): Int
    abstract fun getPageTitle() : String
    abstract fun backButton()

    fun toastS(context: Context, message: String?){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    fun toastL(context: Context, message: String?){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}

