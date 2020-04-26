package com.sinergia.eLibrary.presentation.Favourites.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.Favourites.FavouritesContract
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import kotlinx.android.synthetic.main.layout_headder_bar.*

class FavouritesActivity : BaseActivity(), FavouritesContract.favouritesView {

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { Intent(this, MainMenuActivity::class.java) }

    }

    override fun getLayout(): Int {
        return R.layout.activity_favourites
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_FAVOURITES)
    }

    // CONTRACT VIEW METHODS
    override fun showError(error: String?) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showFavouritesgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun hideFavouritesgressBar() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initFavourites(resourcesList: ArrayList<Resource>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun initFavourites(book: Resource?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToBook(resource: Resource) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun eraseCatalog() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun startScan() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun checkAndSetCamentaPermissions() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    // OVERRIDE METHODS
    /*override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        catalogPresenter.dettachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        catalogPresenter.dettachView()
    }*/

}
