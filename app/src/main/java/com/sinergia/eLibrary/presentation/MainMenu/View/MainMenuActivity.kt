package com.sinergia.eLibrary.presentation.MainMenu.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Account.View.AccountActivity
import com.sinergia.eLibrary.presentation.AdminZone.View.AdminZoneActivity
import com.sinergia.eLibrary.presentation.MainMenu.MainMenuContract
import com.sinergia.eLibrary.presentation.Catalog.View.CatalogActivity
import com.sinergia.eLibrary.presentation.Favourites.view.FavouritesActivity
import com.sinergia.eLibrary.presentation.Libraries.View.LibraiesActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Neurolinguistics.View.NeurolinguisticsActivity
import kotlinx.android.synthetic.main.activity_main_menu.*
import kotlinx.android.synthetic.main.layout_headder_bar.*
import kotlinx.android.synthetic.main.layout_main_menu.*
import com.sinergia.eLibrary.presentation.NeLSProject.Companion as NeLSVars

class MainMenuActivity : BaseActivity(), MainMenuContract.MainContractView {

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_menu)

        showHideAdminZone(NeLSVars.adminUser)

        page_title.text = getPageTitle()

        menu_button.setOnClickListener { finish() }

        library_button.setOnClickListener { goToLibrary() }

        catalog_button.setOnClickListener { goToCatalog() }

        favourites_button.setOnClickListener { goToFavourites() }

        neurolinguistic_button.setOnClickListener { goToNeurolinguisticZone() }

        account_button.setOnClickListener { goToAccount() }

        admin_button.setOnClickListener { goToAdminZone() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_main_menu
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_MAIN)
    }

    override fun backButton() {
        if(NeLSProject.backButtonPressedTwice){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("EXIT", true)
            startActivity(intent)
        } else {
            toastL(this, getString(R.string.BTN_BACK))
            NeLSProject.backButtonPressedTwice = true
        }
    }


    //MAIN MENU CONTRACT METHODS
    override fun showMessage(message: String?) {
        toastS(this, message)
    }

    override fun showError(errorMsg: String?) {
        toastL(this, errorMsg)
    }

    override fun goToLibrary() {
        val librariesIntent = Intent(this, LibraiesActivity::class.java)
        librariesIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(librariesIntent)
        disableAllButtons()
    }

    override fun goToCatalog() {
        val catalogIntent = Intent(this, CatalogActivity::class.java)
        catalogIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(catalogIntent)
        disableAllButtons()
    }

    override fun goToFavourites() {
        val favouritesIntent = Intent(this, FavouritesActivity::class.java)
        favouritesIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(favouritesIntent)
        disableAllButtons()
    }

    override fun goToNeurolinguisticZone() {
        val neurolinguisticIntent = Intent(this, NeurolinguisticsActivity::class.java)
        neurolinguisticIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(neurolinguisticIntent)
        disableAllButtons()
    }

    override fun goToAccount() {
        val accountIntent = Intent(this, AccountActivity::class.java)
        accountIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(accountIntent)
        disableAllButtons()
    }

    override fun goToAdminZone() {
        val adminZoneIntent = Intent(this, AdminZoneActivity::class.java)
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

    override fun showHideAdminZone(admin: Boolean) {
        if(admin){
            admin_button.visibility = View.VISIBLE
        } else {
            admin_button.visibility = View.GONE
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}
