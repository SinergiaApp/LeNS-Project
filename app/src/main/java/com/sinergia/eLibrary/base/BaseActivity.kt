package com.sinergia.eLibrary.base

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
abstract class BaseActivity: AppCompatActivity() {

    fun toast(context: Context, message: String, duration: String){

        if(duration == "l"){
            Toast.makeText(context, message, Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }

    }

}

