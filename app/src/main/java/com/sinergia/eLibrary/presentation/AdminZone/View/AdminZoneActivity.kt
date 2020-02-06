package com.sinergia.eLibrary.presentation.AdminZone.View

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.children
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract.AdminZonePresenter
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl
import com.sinergia.eLibrary.presentation.Catalog.View.CatalogActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import kotlinx.android.synthetic.main.activity_admin_zone.main_page_title
import kotlinx.android.synthetic.main.activity_admin_zone.menu_button
import kotlinx.android.synthetic.main.layout_admin_zone.*
import kotlinx.android.synthetic.main.layout_admin_zone_new_library.*
import kotlinx.android.synthetic.main.layout_admin_zone_new_resource.*
import kotlinx.android.synthetic.main.layout_admin_zone_set_resource.*

class AdminZoneActivity : BaseActivity(), AdminZoneContract.AdminZoneView {

    private lateinit var adminPresenter: AdminZonePresenter
    private lateinit var adminViewModel: AdminViewModelImpl
    private lateinit var getedResource: Resource

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

        admin_zone_setBookSearch_btn.setOnClickListener { getResourceToModify() }
        admin_zone_setResourceButton.setOnClickListener { showHideSetResource() }
        admin_zone_setResource_btn.setOnClickListener { setResource() }

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

    override fun showError(error: String?) {
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
            val editorial = admin_zone_bookPublisher.text.toString()
            val sinopsis = admin_zone_bookSinosis.text.toString()
            val librariesDisponibility: MutableMap<String, Integer> = mutableMapOf()
            val isOnline: Boolean = admin_zone_isOnline.isChecked
            val urlOnline: String = admin_zone_urlOnline.text.toString()

            if(adminPresenter.checkEmptyAddResourceFields(titulo, autor, isbn, edicion, editorial, sinopsis)){

                if(adminPresenter.checkEmptyAddResourceTitle(titulo)){
                    admin_zone_bookTitle.error = "¡Cuidado! El campo 'Título' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceAuthor(autor)){
                    admin_zone_bookAuthor.error = "¡Cuidado! El campo 'Autor' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceEdition(edicion)){
                    admin_zone_bookEdition.error = "¡Cuidado! El campo 'Edición' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourcePublisher(editorial)){
                    admin_zone_bookPublisher.error = "¡Cuidado! El campo 'Editorial' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceISBN(isbn)){
                    admin_zone_bookISBN.error = "¡Cuidado! El campo 'IBAN' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceSinopsis(sinopsis)){
                    admin_zone_bookSinosis.error = "¡Cuidado! El campo 'Sinopsis' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddResourceIsOnline(isOnline, urlOnline)){
                    admin_zone_urlOnline.error = "¡Cuidado! Si el recurso está disponible, tienes que indicar la URL de visionado."
                }

            } else {

                var autores = autor.split(";")

                adminPresenter.addNewResource(titulo, autores, isbn, edicion, editorial, sinopsis, librariesDisponibility, mutableListOf(), mutableListOf(), isOnline, urlOnline)
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

        //SET RESOURCE METHODS
        override fun showHideSetResource() {
            if(admin_zone_setResourceWindow.visibility == View.VISIBLE){
                admin_zone_setResourceWindow.visibility = View.GONE
            } else {
                admin_zone_setResourceWindow.visibility = View.VISIBLE
            }
        }

        override fun showSetResourceProgressBar() {
            admin_zone_setResourceProgressBar.visibility = View.VISIBLE
        }

        override fun hideSetResourceProgressBar() {
            admin_zone_setResourceProgressBar.visibility = View.INVISIBLE
        }

        override fun enableSetResourceButton() {
            admin_zone_setResource_btn.isEnabled = true
            admin_zone_setResource_btn.isClickable = true
        }

        override fun disableSetResourceButton() {
            admin_zone_setResource_btn.isEnabled = false
            admin_zone_setResource_btn.isClickable = false
        }

        override fun enableSearchResourceToModifyButton() {
            admin_zone_setBookSearch_btn.isClickable = true
            admin_zone_setBookSearch_btn.isEnabled = true
        }

        override fun disableSearchResourceToModifyButton() {
            admin_zone_setBookSearch_btn.isClickable = false
            admin_zone_setBookSearch_btn.isEnabled = false
        }

    override fun getResourceToModify() {
            if(admin_zone_setBookSearch.text.isNullOrEmpty()){
                showError("Por favor, indica el ISBN del libro a modificar o escanea un código para continuar.")
            } else {
                adminPresenter.getResourceToModify(admin_zone_setBookSearch.text.toString())
            }

        }

        override fun initSetResourceContent(resource: Resource?, libraries: ArrayList<Library>?) {

            this.getedResource = resource!!
            var autores: String = ""
            for(autor in resource.author){
                autores += "$autor;"
            }
            autores.substring(0, autores.length-1)

            admin_zone_setBookTitle.setText(resource?.title)
            admin_zone_setBookAuthor.setText(autores)
            admin_zone_setBookPublisher.setText(resource?.publisher)
            admin_zone_setBookEdition.setText(resource?.edition)
            admin_zone_setBookISBN.setText(resource?.isbn)
            admin_zone_setBookSinosis.setText(resource?.sinopsis)
            admin_zone_setIsOnline.isChecked = resource?.isOnline!!
            if(resource?.isOnline) admin_zone_setUrlOnline.setText(resource?.urlOnline)

            for(library in libraries!!){

                val libName = TextView(this)
                libName.text = library.name
                val libDisp = EditText(this)
                libDisp.inputType = InputType.TYPE_CLASS_NUMBER
                libDisp.tag = library.id
                if(resource.disponibility.containsKey(library.id)) libDisp.setText(resource.disponibility.get(library.id).toString())

                admin_zone_setBookDisponibility.addView(libName)
                admin_zone_setBookDisponibility.addView(libDisp)

            }

        }


    override fun setResource() {

            val title = admin_zone_setBookTitle.text.toString()
            val author = admin_zone_setBookAuthor.text.toString()
            val publisher = admin_zone_setBookPublisher.text.toString()
            val edition = admin_zone_setBookEdition.text.toString()
            val sinopsis = admin_zone_setBookSinosis.text.toString()
            val isbn = admin_zone_setBookISBN.text.toString()
            val likes = this.getedResource.likes
            val dislikes = this.getedResource.dislikes
            val isOnline = admin_zone_isOnline.isChecked
            val urlOnline = if(isOnline) admin_zone_urlOnline.text.toString() else ""

            if(adminPresenter.checkEmptySetResourceFields(title, author, isbn, edition, publisher, sinopsis)){

                if(adminPresenter.checkEmptySetResourceTitle(title)){
                    admin_zone_setBookTitle.error = "¡Cuidado! El campo 'Título' es obligatorio."
                }

                if(adminPresenter.checkEmptySetResourceAuthor(author)){
                    admin_zone_setBookAuthor.error = "¡Cuidado! El campo 'Autor' es obligatorio."
                }

                if(adminPresenter.checkEmptySetResourceEdition(edition)){
                    admin_zone_setBookEdition.error = "¡Cuidado! El campo 'Edición' es obligatorio."
                }

                if(adminPresenter.checkEmptySetResourcePublisher(publisher)){
                    admin_zone_setBookPublisher.error = "¡Cuidado! El campo 'Editorial' es obligatorio."
                }

                if(adminPresenter.checkEmptySetResourceSinopsis(sinopsis)){
                    admin_zone_setBookSinosis.error = "¡Cuidado! El campo 'Sinopsis' es obligatorio."
                }

                if(adminPresenter.checkEmptySetResourceIsOnline(isOnline, urlOnline)){
                    admin_zone_bookSinosis.error = "¡Cuidado! Si el recurso está disponible, tienes que indicar la URL de visionado."
                }

            } else {

                var authors = author.split(";")

                var disponibility = mutableMapOf<String, Integer>()
                for(view in admin_zone_setBookDisponibility.children){
                    if( view is EditText  ) {
                        val disp = view as EditText
                        var dispNumber = Integer(0)
                        if(disp.text.toString() != "") dispNumber = Integer(disp.text.toString().trim())
                        disponibility.put(view.getTag().toString(), dispNumber)
                    }
                }

                val setedResource = Resource(title, authors, publisher, edition, sinopsis, isbn, disponibility, likes, dislikes, isOnline, urlOnline)

                adminPresenter.setResource(setedResource)

            }

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

            val nombre = admin_zone_libraryName.text.toString()
            val direccion = admin_zone_libraryAddress.text.toString()
            val latitud = admin_zone_libraryGeoPoint1.text.toString()
            val longitud = admin_zone_libraryGeoPoint2.text.toString()


            if(adminPresenter.checkEmptyAddLibraryFields(nombre, direccion, latitud, longitud)){

                if(adminPresenter.checkEmptyAddLibraryName(nombre)){
                    admin_zone_libraryName.error = "¡Cuidado! El campo 'Nombre' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddLibraryAddress(direccion)){
                    admin_zone_libraryAddress.error = "¡Cuidado! El campo 'Dirección' es obligatorio."
                }

                if(adminPresenter.checkEmptyAddLibraryLatitude(latitud)){
                    admin_zone_libraryGeoPoint1.error = "¡Cuidado! El campo 'Latitud' introducido es erróneo, tiene que ser un número con decimales."
                }

                if(adminPresenter.checkEmptyAddLibraryLongitude(longitud)){
                    admin_zone_libraryGeoPoint2.error = "¡Cuidado! El campo 'Logitud' introducido es erróneo, tiene que ser un número con decimales."
                }

            } else {

                showAddLibraryProgressBar()
                disableAddLibraryButton()

                val geopoint = GeoPoint(latitud.toDouble(), longitud.toDouble())

                adminPresenter.addNewLibrary(nombre, direccion, geopoint)
            }

        }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adminPresenter.dettachView()
        adminPresenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        adminPresenter.dettachView()
        adminPresenter.dettachJob()
    }

}
