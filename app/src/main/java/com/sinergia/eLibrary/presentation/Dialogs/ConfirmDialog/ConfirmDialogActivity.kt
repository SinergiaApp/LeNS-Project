package com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinergia.eLibrary.R

class ConfirmDialogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.confirm_dialog_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ConfirmDialogFragment.newInstance())
                .commitNow()
        }
    }

}
