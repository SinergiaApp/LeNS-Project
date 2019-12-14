package com.sinergia.eLibrary.presentation.AdminZone.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract.AdminZonePresenter
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl
import com.sinergia.eLibrary.presentation.Catalog.View.CatalogActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import kotlinx.android.synthetic.main.activity_admin_zone.*
import kotlinx.android.synthetic.main.activity_admin_zone.main_page_title
import kotlinx.android.synthetic.main.activity_admin_zone.menu_button
import kotlinx.android.synthetic.main.activity_catalog.*
import kotlinx.android.synthetic.main.layout_admin_zone.*

class AdminZoneActivity : BaseActivity(), AdminZoneContract.AdminZoneView {


    //ADMIN VIEW MODEL
    private lateinit var adminPresenter: AdminZonePresenter
    private lateinit var adminViewModel: AdminViewModelImpl

    //BASEACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_zone)

        main_page_title.text = getPageTitle()

        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        adminPresenter = com.sinergia.eLibrary.presentation.AdminZone.Presenter.AdminZonePresenter(AdminViewModelImpl())
        adminPresenter.attachView(this)
        adminViewModel = ViewModelProviders.of(this).get(AdminViewModelImpl::class.java)

        admin_zone_addResourceButton.setOnClickListener { showHideAddResource() }
        admin_zone_addNewResourceButton.setOnClickListener { createNewResource() }

        admin_zone_addLibraryButton.setOnClickListener { showHideAddLibrary() }
        admin_zone_addNewLibraryButton.setOnClickListener { createNewLibrary() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_admin_zone
    }

    override fun getPageTitle(): String {
        return "Zona de Administración"
    }

    //ADMIN ZONE CONTRACT METHODS

    override fun showError(error: String) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastL(this,message)
    }

    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, CatalogActivity::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }

        //CREATE RESOURCE METHODS
        override fun showHideAddResource() {
            if(admin_zone_addResourceWindow.visibility == View.GONE){
                admin_zone_addResourceWindow.visibility = View.VISIBLE
            } else {
                admin_zone_addResourceWindow.visibility = View.GONE
            }
        }

        override fun createNewResource() {

            showAddResourceProgressBar()
            disableAddResourceButton()

            val titulo = admin_zone_bookTitle.text.toString()
            val autor = admin_zone_bookAuthor.text.toString()
            val isbn = admin_zone_bookISBN.text.toString()
            val edicion = admin_zone_bookEdition.text.toString()
            val sinopsis = admin_zone_bookSinosis.text.toString()

            if(adminPresenter.checkEmptyAddResourceFields(titulo, autor, edicion, isbn, sinopsis)){

                if(adminPresenter.checkEmptyAddResourceTitle(titulo)){
                    admin_zone_bookTitle.error = "¡Cuidado! El campo 'Título' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceAuthor(autor)){
                    admin_zone_bookAuthor.error = "¡Cuidado! El campo 'Autor' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceEdition(edicion)){
                    admin_zone_bookISBN.error = "¡Cuidado! El campo 'Edición' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceIBAN(isbn)){
                    admin_zone_bookEdition.error = "¡Cuidado! El campo 'IBAN' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceSinopsis(sinopsis)){
                    admin_zone_bookSinosis.error = "¡Cuidado! El campo 'Sinopsis' es obligatorio."
                }

                hideAddResourceProgressBar()
                enableAddResourceButton()

            } else {
                adminPresenter.addNewResource(titulo, autor, isbn, edicion, sinopsis)
            }

        }

        override fun showAddResourceProgressBar() {
            admin_zone_newResourceProgressBar.visibility = View.VISIBLE
        }

        override fun hideAddResourceProgressBar() {
            admin_zone_newResourceProgressBar.visibility = View.GONE
        }

        override fun enableAddResourceButton() {
            admin_zone_addNewResourceButton.isEnabled = true
            admin_zone_addNewResourceButton.isClickable = true
        }

        override fun disableAddResourceButton() {
            admin_zone_addNewResourceButton.isEnabled = false
            admin_zone_addNewResourceButton.isClickable = false
        }


        //CREATE LIBRARY METHODS
        override fun showHideAddLibrary() {
            if(admin_zone_addLibraryWindow.visibility == View.GONE){
                admin_zone_addLibraryWindow.visibility = View.VISIBLE
            } else {
                admin_zone_addLibraryWindow.visibility = View.GONE
            }
        }

        override fun showAddLibraryProgressBar() {
            admin_zone_newLibraryProgressBar.visibility = View.VISIBLE
        }

        override fun hideAddLibraryProgressBar() {
            admin_zone_addNewLibraryButton.visibility = View.GONE
        }

        override fun enableAddLibraryButton() {
            admin_zone_addNewLibraryButton.isEnabled = true
            admin_zone_addNewLibraryButton.isClickable = true
        }

        override fun disableAddLibraryButton() {
            admin_zone_addNewLibraryButton.isEnabled = false
            admin_zone_addNewLibraryButton.isClickable = false
        }

        override fun createNewLibrary() {

            showAddLibraryProgressBar()
            disableAddLibraryButton()

            val nombre = admin_zone_libraryName.toString()
            val direccion = admin_zone_libraryAddress.toString()
            val latitud = admin_zone_libraryGeoPoint1.toString().toDouble()
            val longitud = admin_zone_libraryGeoPoint2.toString().toDouble()
            val geopoint = GeoPoint(latitud, longitud)

            if(adminPresenter.checkEmptyAddLibraryFields(nombre, direccion, latitud, longitud)){

                if(adminPresenter.checkEmptyAddLibraryName(nombre)){
                    admin_zone_libraryName.error = "¡Cuidado! El campo 'Nombre' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddLibraryAddress(direccion)){
                    admin_zone_libraryAddress.error = "¡Cuidado! El campo 'Dirección' es obligatorio."
                }

                if(adminPresenter.checkWrongAddLibraryLatitude(latitud)){
                    admin_zone_libraryGeoPoint1.error = "¡Cuidado! El campo 'Latitud' introducido es erróneo, tiene que ser un número con decimales."
                }

                if(adminPresenter.checkWrongAddLibraryLongitude(longitud)){
                    admin_zone_libraryGeoPoint2.error = "¡Cuidado! El campo 'Logitud' introducido es erróneo, tiene que ser un número con decimales."
                }

            } else {
                adminPresenter.addNewLibrary(nombre, direccion, geopoint)
            }

        }



}
