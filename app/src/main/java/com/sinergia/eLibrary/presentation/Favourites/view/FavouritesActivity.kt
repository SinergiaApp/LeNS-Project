package com.sinergia.eLibrary.presentation.Favourites.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.common.base.Strings
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.CameraScan.View.CameraScanActivity
import com.sinergia.eLibrary.presentation.Catalog.View.ItemCatalogActivity
import com.sinergia.eLibrary.presentation.Favourites.FavouritesContract
import com.sinergia.eLibrary.presentation.Favourites.model.FavouritesViewModel
import com.sinergia.eLibrary.presentation.Favourites.model.FavouritesViewModelImpl
import com.sinergia.eLibrary.presentation.Favourites.presenter.FavouritesPresenter
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.utils.CreateCards
import kotlinx.android.synthetic.main.activity_favourites.*
import kotlinx.android.synthetic.main.layout_headder_bar.*

class FavouritesActivity : BaseActivity(), FavouritesContract.FavouritesView {

    private lateinit var favouritesPresenter: FavouritesContract.FavouritesPresenter
    private lateinit var favouritesViewModel: FavouritesViewModel

    private var buttonRequestCameraPermission = false

    private val cardUtils = CreateCards()

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(getLayout())

        favouritesPresenter = FavouritesPresenter(FavouritesViewModelImpl())
        favouritesPresenter.attachView(this)
        favouritesViewModel = ViewModelProviders.of(this).get(FavouritesViewModelImpl::class.java)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }
        favourites_search_btn.setOnClickListener {
            if(Strings.isNullOrEmpty(favourites_search.text.toString())){
                favouritesPresenter.getAllFavouriteResourcesToCatalog()
            } else {
                favouritesPresenter.getFavouriteResourceToCatalog(favourites_search.text.toString())
            }
        }

        favourites_camera_btn.setOnClickListener { startScan() }

        favouritesPresenter.getAllFavouriteResourcesToCatalog()

    }

    override fun getLayout(): Int {
        return R.layout.activity_favourites
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_FAVOURITES)
    }


    // CAMERA METHODS
    override fun startScan() {
        if(NeLSProject.cameraPermissionGranted){
            val scanIntent = Intent(this, CameraScanActivity::class.java)
            startActivityForResult(scanIntent, NeLSProject.CAMERA_INTENT_CODE)
        } else {
            toastL(this, "Por favor permite que la app acceda a la cámara.")
            checkAndSetCamentaPermissions()
        }

    }

    override fun checkAndSetCamentaPermissions() {
        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if(permissionStatus == PackageManager.PERMISSION_GRANTED ) {
            NeLSProject.cameraPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), NeLSProject.CAMERA_PERMISSIONS_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == NeLSProject.CAMERA_INTENT_CODE && resultCode == Activity.RESULT_OK){

            if(data != null){
                var resultBarCode = data.getStringExtra("codigo")
                favourites_search.setText(resultBarCode)
            } else {
                toastL(this, "Imposible leer el código, vuelve a intentarlo.")
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            NeLSProject.CAMERA_PERMISSIONS_CODE ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (buttonRequestCameraPermission) startScan()
                    NeLSProject.cameraPermissionGranted = true
                } else {
                    toastL(this, "El escaneo no se podrá llevar a cabo hasta que no concedas los permisos de usar la cámara.")
                }
        }
    }

    // CONTRACT VIEW METHODS
    override fun showError(error: String?) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showFavouritesProgressBar() {
        favourites_progressBar.visibility = View.VISIBLE
    }

    override fun hideFavouritesgressBar() {
        favourites_progressBar.visibility = View.GONE
    }

    override fun initFavourites(resourcesList: List<Resource>) {

        if(resourcesList.isEmpty()){
            toastL(this, "Vaya... Parece que aún no tienes favoritos.")
        } else {
            for(resource in resourcesList){
                var resourceCard = cardUtils.createResourceCard(this, resource)
                resourceCard.setOnClickListener { navigateToBook(resource) }
                favourites_content.addView(resourceCard)
            }
        }

    }

    override fun initFavourites(book: Resource) {

        if(book == null){
            toastL(this, "Vaya... Parece que no tienes este libro como favorito")
        } else {
            var resourceCard = cardUtils.createResourceCard(this, book)
            resourceCard.setOnClickListener { navigateToBook(book) }
            favourites_content.addView(resourceCard)
        }

    }

    override fun navigateToBook(resource: Resource) {
        NeLSProject.currentResource = resource
        startActivity(Intent(this, ItemCatalogActivity::class.java))
    }

    override fun eraseFavourites() {
        favourites_content.removeAllViews()
    }

    // OVERRIDE METHODS
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        favouritesPresenter.dettachView()
        favouritesPresenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        favouritesPresenter.dettachView()
        favouritesPresenter.dettachJob()
    }

}
