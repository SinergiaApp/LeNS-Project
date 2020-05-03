package com.sinergia.eLibrary.presentation.Catalog.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Gravity
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import kotlinx.android.synthetic.main.activity_catalog.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.common.base.Strings
import com.google.firebase.auth.FirebaseAuth
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.CameraScan.View.CameraScanActivity
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModelImpl
import com.sinergia.eLibrary.presentation.Catalog.Presenter.CatalogPresenter
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.utils.CreateCards
import kotlinx.android.synthetic.main.layout_headder_bar.*
import kotlin.collections.ArrayList


class CatalogActivity: BaseActivity(), CatalogContract.CatalogView {

    private lateinit var catalogPresenter: CatalogContract.CatalogPresenter
    private lateinit var catalogViewModel: CatalogViewModel

    private var buttonRequestCameraPermission = false

    private val cardUtils = CreateCards()

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catalogPresenter = CatalogPresenter(CatalogViewModelImpl())
        catalogPresenter.attachView(this)
        catalogViewModel = ViewModelProviders.of(this).get(CatalogViewModelImpl::class.java)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }
        catalog_search_btn.setOnClickListener {
            if(Strings.isNullOrEmpty(catalog_search.text.toString())){
                catalogPresenter.getAllResourcesToCatalog()
            } else {
                catalogPresenter.getResourceToCatalog(catalog_search.text.toString())
            }
        }

        catalog_camera_btn.setOnClickListener { startScan() }

        catalogPresenter.getAllResourcesToCatalog()

    }

    override fun getLayout(): Int {
        return R.layout.activity_catalog
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_CATALOG)
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
                catalog_search.setText(resultBarCode)
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

    //CATALOG CONTRACT METHODS
    override fun showError(error: String?) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun showCatalogrogressBar() {
        catalog_progressBar.visibility = View.VISIBLE
    }

    override fun hideCatalogProgressBar() {
        catalog_progressBar.visibility = View.GONE
    }

    override fun initCatalog(resourcesList: ArrayList<Resource>?) {

        if(resourcesList?.isEmpty()!!){
            toastL(this, "Vaya... Parece que no hay ningún recurso en la Base de Datos...")
        } else {

            for(resource in resourcesList!!){
                var resourceCard = cardUtils.createResourceCard(this, resource)
                resourceCard.setOnClickListener { navigateToBook(resource) }
                catalog_content.addView(resourceCard)
            }

        }
    }

    override fun initCatalog(book: Resource?) {
        if(book == null){
            toastL(this, "Vaya... Parece que no hay ningún recurso en la Base de Datos con esos parámetros...")
        } else {
            var resourceCard = cardUtils.createResourceCard(this, book)
            resourceCard.setOnClickListener { navigateToBook(book) }
            catalog_content.addView(resourceCard)
        }
    }

    override fun goToMainMenu() {
        val mainMenuIntent = Intent(this, MainMenuActivity::class.java)
        val activityName : String = getPageTitle()
        mainMenuIntent.putExtra("activityName", activityName)
        startActivity(mainMenuIntent)
    }

    override fun navigateToBook(resource: Resource) {
        NeLSProject.currentResource = resource
        startActivity(Intent(this, ItemCatalogActivity::class.java))
    }

    override fun eraseCatalog() {
        catalog_content.removeAllViews()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        catalogPresenter.dettachView()
        catalogPresenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        catalogPresenter.dettachView()
        catalogPresenter.dettachJob()
    }

}

