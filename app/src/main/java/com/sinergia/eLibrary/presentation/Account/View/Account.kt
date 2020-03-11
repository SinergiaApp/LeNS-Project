package com.sinergia.eLibrary.presentation.Account.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity

class Account : BaseActivity() {

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayout(): Int {
        return R.layout.activity_account
    }

    override fun getPageTitle(): String {
        return "Mi Cuenta"
    }


}
