package com.sinergia.eLibrary.presentation.AdminZone.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import kotlinx.android.synthetic.main.activity_admin_zone.*
import kotlinx.android.synthetic.main.layout_admin_zone.*

class AdminZone : BaseActivity(), AdminZoneContract.AdminZoneView {

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

    //ADMIN ZONE CONTRACT VIEW METHODS
    override fun showHideAddBook() {
        if(admin_zone_addResourceWindow.visibility == View.GONE){
            admin_zone_addResourceWindow.visibility = View.VISIBLE
        } else {
            admin_zone_addResourceWindow.visibility = View.GONE
        }
    }


}
