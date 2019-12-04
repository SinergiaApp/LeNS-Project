package com.sinergia.eLibrary.presentation.AdminZone.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import kotlinx.android.synthetic.main.activity_admin_zone.*

class AdminZone : BaseActivity() {

    //BASEACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_zone)

        main_page_title.text = getPageTitle()

    }

    override fun getLayout(): Int {
        return R.layout.activity_admin_zone
    }

    override fun getPageTitle(): String {
        return "Zona de Administración"
    }


}
