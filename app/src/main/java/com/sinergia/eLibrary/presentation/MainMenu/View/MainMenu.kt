package com.sinergia.eLibrary.presentation.MainMenu.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.AdminZone.View.AdminZone
import com.sinergia.eLibrary.presentation.MainMenu.MainMenuContract
import com.sinergia.eLibrary.presentation.MainPage.View.MainPage
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.layout_main_menu.*

class MainMenu : BaseActivity(), MainMenuContract.MainContractView {

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        main_page_title.text = intent.getStringExtra("activityName")

        menu_button.setOnClickListener { finish() }

        library_button.setOnClickListener { goToLibrary() }

        catalog_button.setOnClickListener { goToCatalog() }

        neurolinguistic_button.setOnClickListener { goToNeurolinguisticZone() }

        account_button.setOnClickListener { goToAccount() }

        admin_button.setOnClickListener { goToAdminZone() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_main_menu
    }

    override fun getPageTitle(): String {
        return "MENU PRINCIPAL"
    }


    //MAIN MENU CONTRACT METHODS
    override fun goToLibrary() {
        //val libraryIntent = Intent(this, ------::class.java)
        //libraryIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        //startActivity(libraryIntent)
        //disableAllButtons()
        toastL(this, "Este botón aún no te lleva a ningun sitio pringao.")
    }

    override fun goToCatalog() {
        val catalogIntent = Intent(this, MainPage::class.java)
        catalogIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(catalogIntent)
        disableAllButtons()
    }

    override fun goToNeurolinguisticZone() {
        //val neurolinguisticIntent = Intent(this, ------::class.java)
        //neurolinguisticIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        //startActivity(neurolinguisticIntent)
        //disableAllButtons()
        toastL(this, "Este botón aún no te lleva a ningun sitio pringao.")
    }

    override fun goToAccount() {
        //val accountIntent = Intent(this, ------::class.java)
        //accountIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        //startActivity(accountIntent)
        //disableAllButtons()
        toastL(this, "Este botón aún no te lleva a ningun sitio pringao.")
    }

    override fun goToAdminZone() {
        val adminZoneIntent = Intent(this, AdminZone::class.java)
        adminZoneIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(adminZoneIntent)
        disableAllButtons()
    }

    override fun disableAllButtons() {

        library_button.isClickable = false
        catalog_button.isClickable = false
        neurolinguistic_button.isClickable = false
        account_button.isClickable = false
        admin_button.isClickable = false

    }

    override fun enableAllButtons() {

        library_button.isClickable = true
        catalog_button.isClickable = true
        neurolinguistic_button.isClickable = true
        account_button.isClickable = true
        admin_button.isClickable = true

    }

}
