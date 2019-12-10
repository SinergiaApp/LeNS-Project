package com.sinergia.eLibrary.presentation.AdminZone.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract.AdminZonePresenter
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl
import com.sinergia.eLibrary.presentation.Catalog.View.CatalogActivity
import kotlinx.android.synthetic.main.activity_admin_zone.*
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

        adminPresenter = com.sinergia.eLibrary.presentation.AdminZone.Presenter.AdminZonePresenter(AdminViewModelImpl())
        adminPresenter.attachView(this)
        adminViewModel = ViewModelProviders.of(this).get(AdminViewModelImpl::class.java)

        admin_zone_addResourceButton.setOnClickListener { showHideAddResource() }
        admin_zone_addNewResourceButton.setOnClickListener { createNewResource() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_admin_zone
    }

    override fun getPageTitle(): String {
        return "Zona de Administración"
    }

    //ADMIN ZONE CONTRACT METHODS
    override fun showHideAddResource() {
        if(admin_zone_addResourceWindow.visibility == View.GONE){
            admin_zone_addResourceWindow.visibility = View.VISIBLE
        } else {
            admin_zone_addResourceWindow.visibility = View.GONE
        }
    }

    override fun createNewResource() {

        showProgressBar()
        disableAddResourceButton()

        val titulo = admin_zone_bookTitle.text.toString()
        val autor = admin_zone_bookAuthor.text.toString()
        val isbn = admin_zone_bookISBN.text.toString()
        val edicion = admin_zone_bookEdition.text.toString()
        val sinopsis = admin_zone_bookSinosis.text.toString()

        if(adminPresenter.checkEmptyFields(titulo, autor, edicion, isbn, sinopsis)){

            if(adminPresenter.checkEmptyTitle(titulo)){
                admin_zone_bookTitle.error = "¡Cuidado! El campo 'Título' es obligatorio."
            }

            if(adminPresenter.checkEmptyAuthor(autor)){
                admin_zone_bookAuthor.error = "¡Cuidado! El campo 'Autor' es obligatorio."
            }

            if(adminPresenter.checkEmptyEdition(edicion)){
                admin_zone_bookISBN.error = "¡Cuidado! El campo 'Edición' es obligatorio."
            }

            if(adminPresenter.checkEmptyIBAN(isbn)){
                admin_zone_bookEdition.error = "¡Cuidado! El campo 'IBAN' es obligatorio."
            }

            if(adminPresenter.checkEmptySinopsis(sinopsis)){
                admin_zone_bookSinosis.error = "¡Cuidado! El campo 'Sinopsis' es obligatorio."
            }

            hideProgressBar()
            enableAddResourceButton()

        } else {
            adminPresenter.addNewResource(titulo, autor, isbn, edicion, sinopsis)
        }

    }

    override fun showError(error: String) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastL(this,message)
    }

    override fun showProgressBar() {
        admin_zone_progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        admin_zone_progressBar.visibility = View.GONE
    }

    override fun enableAddResourceButton() {
        admin_zone_addNewResourceButton.isEnabled = true
        admin_zone_addNewResourceButton.isClickable = true
    }

    override fun disableAddResourceButton() {
        admin_zone_addNewResourceButton.isEnabled = false
        admin_zone_addNewResourceButton.isClickable = false
    }

    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, CatalogActivity::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }



}
