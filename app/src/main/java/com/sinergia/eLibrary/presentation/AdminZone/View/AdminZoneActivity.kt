package com.sinergia.eLibrary.presentation.AdminZone.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Loan
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract
import com.sinergia.eLibrary.presentation.AdminZone.AdminZoneContract.AdminZonePresenter
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModelImpl
import com.sinergia.eLibrary.presentation.CameraScan.View.CameraScanActivity
import com.sinergia.eLibrary.presentation.Catalog.View.CatalogActivity
import com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog.ConfirmDialogActivity
import com.sinergia.eLibrary.presentation.Libraries.View.LibraiesActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.android.synthetic.main.layout_admin_zone.*
import kotlinx.android.synthetic.main.layout_admin_zone_loans.*
import kotlinx.android.synthetic.main.layout_admin_zone_new_library.*
import kotlinx.android.synthetic.main.layout_admin_zone_new_resource.*
import kotlinx.android.synthetic.main.layout_admin_zone_set_library.*
import kotlinx.android.synthetic.main.layout_admin_zone_set_resource.*
import kotlinx.android.synthetic.main.layout_admin_zone_set_resource.admin_zone_setBookSearch_btn
import kotlinx.android.synthetic.main.layout_headder_bar.*

class AdminZoneActivity : BaseActivity(), AdminZoneContract.AdminZoneView {

    private lateinit var adminPresenter: AdminZonePresenter
    private lateinit var adminViewModel: AdminViewModelImpl
    private lateinit var getedResource: Resource

    private lateinit var fillField: String
    private var cameraPermissionGranted = false
    private var buttonRequestCameraPermission = false

    private var reserveChecked: Reserve ?= null
    private var loanChecked: Loan ?= null

    //BASEACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_zone)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        adminPresenter = com.sinergia.eLibrary.presentation.AdminZone.Presenter.AdminZonePresenter(AdminViewModelImpl())
        adminPresenter.attachView(this)
        adminViewModel = ViewModelProviders.of(this).get(AdminViewModelImpl::class.java)

        admin_zone_addResourceButton.setOnClickListener { showHideAddResource() }
        admin_zone_addNewResourceButton.setOnClickListener { createNewResource() }

        admin_zone_setBookSearch_icon2.setOnClickListener { startScan("setResource") }
        admin_zone_setBookSearch_btn.setOnClickListener { getResourceToModify() }
        admin_zone_setResourceButton.setOnClickListener { showHideSetResource() }
        admin_zone_setResource_btn.setOnClickListener { setResource() }

        admin_zone_addLibraryButton.setOnClickListener { showHideAddLibrary() }
        admin_zone_addNewLibraryButton.setOnClickListener { createNewLibrary() }

        admin_zone_setLibrarySearch_icon2.setOnClickListener { startScan("setLibrary") }
        admin_zone_setLibrarySearch_btn.setOnClickListener { getLibraryToModify() }
        admin_zone_setLibraryButton.setOnClickListener { showHideSetLibrary() }
        admin_zone_setLibrary_btn.setOnClickListener { setLibrary() }

        admin_zone_loanManagementButton.setOnClickListener { showHideLoans() }
        admin_zone_loansSearch_btn.setOnClickListener { getUserLoansAndReserves() }
        admin_zone_LoansSearch_icon2.setOnClickListener { startScan("loansManagement") }
        admin_zone_loansContent_initLoan_btn.setOnClickListener { initLoan() }
        admin_zone_loansConten_cancelReserve_btn.setOnClickListener { cancelReserve() }
        admin_zone_loansContent_enlargeLoan_btn.setOnClickListener { enlargeLoan() }
        admin_zone_loansConten_finalizeLoan_btn.setOnClickListener { finalizeLoan() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_admin_zone
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_ADMIN_ZONE)
    }

    // CAMERA METHODS
    override fun startScan(field: String) {

        fillField = field

        if(cameraPermissionGranted){
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
            cameraPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), NeLSProject.CAMERA_PERMISSIONS_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == NeLSProject.CAMERA_INTENT_CODE && resultCode == Activity.RESULT_OK){

            if(data != null){
                var resultBarCode = data.getStringExtra("codigo")
                if(fillField == "setResource") admin_zone_setBookSearch.setText(resultBarCode)
                if(fillField == "setLibrary") admin_zone_setLibrarySearch.setText(resultBarCode)
                if(fillField == "loansManagement") admin_zone_loansSearch.setText(resultBarCode)
            } else {
                toastL(this, "Imposible leer el código, vuelve a intentarlo.")
            }

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            NeLSProject.CAMERA_PERMISSIONS_CODE ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (buttonRequestCameraPermission) startScan(fillField)
                    cameraPermissionGranted = true
                } else {
                    toastL(this, "El escaneo no se podrá llevar a cabo hasta que no concedas los permisos de usar la cámara.")
                }
        }
    }

    //ADMIN ZONE CONTRACT METHODS
    override fun showError(error: String?) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastL(this,message)
    }

    override fun navigateToCatalog() {
        val intentcatalogPage = Intent(this, CatalogActivity::class.java)
        intentcatalogPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentcatalogPage)
    }

    override fun navigateToLibraries() {
        val intentLibrariesPage = Intent(this, LibraiesActivity::class.java)
        intentLibrariesPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentLibrariesPage)
    }

    override fun navigateToAdminZone() {
        val intentAdminZonePage = Intent(this, AdminZoneActivity::class.java)
        intentAdminZonePage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentAdminZonePage)
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
            admin_zone_newResourceProgressBar.visibility = View.INVISIBLE
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

        override fun showSetResouceContent() {
            admin_zone_setResourceContent.visibility = View.VISIBLE
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

            admin_zone_setBookTitle.setText(resource.title)
            admin_zone_setBookAuthor.setText(autores)
            admin_zone_setBookPublisher.setText(resource.publisher)
            admin_zone_setBookEdition.setText(resource.edition)
            admin_zone_setBookISBN.setText(resource.isbn)
            admin_zone_setBookSinosis.setText(resource.sinopsis)
            admin_zone_setIsOnline.isChecked = resource.isOnline
            if(resource.isOnline) admin_zone_setUrlOnline.setText(resource.urlOnline)

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

                var disponibility = mutableMapOf<String, Int>()
                for(view in admin_zone_setBookDisponibility.children){
                    if( view is EditText  ) {
                        val disp = view as EditText
                        var dispNumber = 0
                        if(disp.text.toString() != "") dispNumber = disp.text.toString().trim().toInt()
                        disponibility[view.getTag().toString()] = dispNumber
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
            admin_zone_addNewLibraryButton.visibility = View.INVISIBLE
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

            } else if(adminPresenter.checkInRangeAddLibraryGeopoints(latitud, longitud)) {

                if (adminPresenter.checkInRangeAddLibraryLatitude(latitud)) {
                    admin_zone_libraryGeoPoint1.error ="¡Cuidado! El campo 'Latitud' introducido es erróneo, tiene que estar comprendido entre -180.0 y 180.0."
                }

                if (adminPresenter.checkInRangeAddLibraryLongitude(longitud)) {
                    admin_zone_libraryGeoPoint2.error ="¡Cuidado! El campo 'Longitud' introducido es erróneo, tiene que estar comprendido entre -180.0 y 180.0."
                }

            } else {

                showAddLibraryProgressBar()
                disableAddLibraryButton()

                val geopoint = GeoPoint(latitud.toDouble(), longitud.toDouble())

                adminPresenter.addNewLibrary(nombre, direccion, geopoint)
            }

        }

        // SET LIBRARY METHODS
        override fun showHideSetLibrary() {
            if(admin_zone_setLibraryWindow.visibility == View.VISIBLE){
                admin_zone_setLibraryWindow.visibility = View.GONE
            } else {
                admin_zone_setLibraryWindow.visibility = View.VISIBLE
            }
        }

        override fun showSetLibraryProgressBar() {
            admin_zone_setLibraryProgressBar.visibility = View.VISIBLE
        }

        override fun hideSetLibraryProgressBar() {
            admin_zone_setLibraryProgressBar.visibility = View.INVISIBLE
        }

        override fun enableSearchLibraryToModifyButton() {
            admin_zone_setLibrarySearch_btn.isEnabled = true
            admin_zone_setLibrarySearch_btn.isClickable = true
        }

        override fun disableSearchLibraryToModifyButton() {
            admin_zone_setLibrarySearch_btn.isEnabled = false
            admin_zone_setLibrarySearch_btn.isClickable = false
        }

        override fun enableSetLibraryButton() {
            admin_zone_setLibrary_btn.isEnabled = true
            admin_zone_setLibrary_btn.isClickable = true
        }

        override fun disableSetLibraryButton() {
            admin_zone_setLibrary_btn.isEnabled = false
            admin_zone_setLibrary_btn.isClickable = false
        }

        override fun showSetLibraryContent() {
            admin_zone_setLibraryContent.visibility = View.VISIBLE
        }

        override fun getLibraryToModify() {
            if(admin_zone_setLibrarySearch.text.isNullOrEmpty()){
                showError("Por favor, indica el ID de la Biblioteca a modificar o escanea un código para continuar.")
            } else {
                adminPresenter.getLibraryToModify(admin_zone_setLibrarySearch.text.toString())
            }
        }

        override fun initLibraryContent(library: Library?) {

            admin_zone_setLibraryName_title.setText(library?.name.toString())
            admin_zone_setLibraryId.setText(library?.id.toString())
            admin_zone_setLibraryName.setText(library?.name.toString())
            admin_zone_setLibraryAddress.setText(library?.address.toString())
            admin_zone_setLibraryGeoPoint1.setText(library?.geopoint?.latitude.toString())
            admin_zone_setLibraryGeoPoint2.setText(library?.geopoint?.longitude.toString())

        }

        override fun setLibrary() {

            val id = admin_zone_setLibraryId.text.toString()
            val name = admin_zone_setLibraryName.text.toString()
            val address = admin_zone_setLibraryAddress.text.toString()
            val latitude = admin_zone_setLibraryGeoPoint1.text.toString()
            val longitude = admin_zone_setLibraryGeoPoint2.text.toString()

            if(adminPresenter.checkEmptyAddLibraryFields(name, address, latitude, longitude)){

                if(adminPresenter.checkEmptySetLibraryName(name)){
                    admin_zone_setLibraryName.error = "¡Cuidado! El campo 'Nombre' es obligatorio."
                }

                if(adminPresenter.checkEmptySetLibraryAddress(address)){
                    admin_zone_setLibraryAddress.error = "¡Cuidado! El campo 'Dirección' es obligatorio."
                }

                if(adminPresenter.checkEmptySetLibraryLatitude(latitude)){
                    admin_zone_setLibraryGeoPoint1.error = "¡Cuidado! El campo 'Latitud' es obligatorio."
                }

                if(adminPresenter.checkEmptySetLibraryLongitude(longitude)){
                    admin_zone_setLibraryGeoPoint2.error = "¡Cuidado! El campo 'Logitud' es obligatorio."
                }

            } else if(adminPresenter.checkInRangeSetLibraryGeopoints(latitude, longitude)) {

                if (adminPresenter.checkInRangeSetLibraryLatitude(latitude)) {
                    admin_zone_setLibraryGeoPoint1.error ="¡Cuidado! El campo 'Latitud' introducido es erróneo, tiene que estar comprendido entre -180.0 y 180.0."
                }

                if (adminPresenter.checkInRangeSetLibraryLogitude(longitude)) {
                    admin_zone_setLibraryGeoPoint2.error ="¡Cuidado! El campo 'Longitud' introducido es erróneo, tiene que estar comprendido entre -180.0 y 180.0."
                }

            } else {

                val settedGeopoint = GeoPoint(latitude.toDouble(), longitude.toDouble())

                val settedLibrary = Library(id, name, address, settedGeopoint)

                adminPresenter.setLibrary(settedLibrary)

            }

        }

    //LOANS METHODS
    override fun showHideLoans() {
        if(admin_zone_loanManagement.visibility == View.GONE){
            admin_zone_loanManagement.visibility = View.VISIBLE
        } else {
            admin_zone_loanManagement.visibility = View.GONE
        }
    }

    override fun showLoansManagementContent() {
        admin_zone_loansContent.visibility = View.VISIBLE
    }

    override fun showLoanManagementProgressBar() {
        admin_zone_loansProgressBar.visibility = View.VISIBLE
    }

    override fun hideLoanManagementProgressBar() {
        admin_zone_loansProgressBar.visibility = View.GONE
    }

    override fun enableSearchLoanButton() {
        admin_zone_loansSearch_btn.isClickable = true
        admin_zone_loansSearch_btn.isEnabled = true
    }

    override fun disableSearchLoanButton() {
        admin_zone_loansSearch_btn.isClickable = false
        admin_zone_loansSearch_btn.isEnabled = false
    }

    override fun disableAllLoanReserveButtons() {
        admin_zone_loansContent_initLoan_btn.isClickable = false
        admin_zone_loansContent_initLoan_btn.isEnabled = false
        admin_zone_loansConten_cancelReserve_btn.isClickable = false
        admin_zone_loansConten_cancelReserve_btn.isEnabled = false
        admin_zone_loansContent_enlargeLoan_btn.isClickable = false
        admin_zone_loansContent_enlargeLoan_btn.isEnabled = false
        admin_zone_loansConten_finalizeLoan_btn.isClickable = false
        admin_zone_loansConten_finalizeLoan_btn.isEnabled = false
    }


    override fun enableInitLoanButton() {
        admin_zone_loansContent_initLoan_btn.isClickable = true
        admin_zone_loansContent_initLoan_btn.isEnabled = true
    }

    override fun disableInitLoanButton() {
        admin_zone_loansContent_initLoan_btn.isClickable = false
        admin_zone_loansContent_initLoan_btn.isEnabled = false
    }

    override fun enableCancelReserveButton() {
        admin_zone_loansConten_cancelReserve_btn.isClickable = true
        admin_zone_loansConten_cancelReserve_btn.isEnabled = true
    }

    override fun disableCancelReserveButton() {
        admin_zone_loansConten_cancelReserve_btn.isClickable = false
        admin_zone_loansConten_cancelReserve_btn.isEnabled = false
    }

    override fun enableEnlargeLoanButton() {
        admin_zone_loansContent_enlargeLoan_btn.isClickable = true
        admin_zone_loansContent_enlargeLoan_btn.isEnabled = true
    }

    override fun disableEnlargeLoanButton() {
        admin_zone_loansContent_enlargeLoan_btn.isClickable = false
        admin_zone_loansContent_enlargeLoan_btn.isEnabled = false
    }

    override fun enableFinalizeLoanButton() {
        admin_zone_loansConten_finalizeLoan_btn.isClickable = true
        admin_zone_loansConten_finalizeLoan_btn.isEnabled = true
    }

    override fun disableFinalizeLoanButton() {
        admin_zone_loansConten_finalizeLoan_btn.isClickable = false
        admin_zone_loansConten_finalizeLoan_btn.isEnabled = false
    }

    override fun getUserLoansAndReserves() {

        val userMail = admin_zone_loansSearch.text.toString()

        if(adminPresenter.checkEmptyLoanManagementMail(userMail)){
            admin_zone_loansSearch.error = "¡Cuidado! El campo 'Usuario' es obligatorio."
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }

        if(adminPresenter.checkValidLoanManagementMail(userMail)) {
            admin_zone_loansSearch.error = "¡Cuidado! El Correo Electrónico introducido no es válido."
            toastS(this, "Vaya... Hay errores en los campos introducidos.")
            return
        }

        adminPresenter.getUserLoansAndReserves(userMail)

    }

    override fun initLoansManagementContent(reserves: List<Reserve>, loans: List<Loan>) {

        admin_zone_loansContent_reserves_list.removeAllViews()
        if(reserves.isNotEmpty()) admin_zone_loansContent_reserves_noList.visibility = View.GONE
        for(reserve in reserves){

            val reserveText = "ISBN: ${reserve.resourceId}.\nTítulo: ${reserve.resourceName}.\nBiblioteca: ${reserve.libraryId}."
            var reserveRadio = RadioButton(this)
            reserveRadio.text = reserveText
            reserveRadio.setOnClickListener { this.reserveChecked = reserve }
            admin_zone_loansContent_reserves_list.addView(reserveRadio)

        }

        admin_zone_loansContent_loans_list.removeAllViews()
        if(loans.isNotEmpty())admin_zone_loansContent_loans_noList.visibility = View.GONE
        for(loan in loans){

            val loanText = "ISBN: ${loan.resourceId}.\nTítulo: ${loan.resourceName}.\nBiblioteca: ${loan.libraryId}."
            var loanRadio = RadioButton(this)
            loanRadio.text = loanText
            loanRadio.setOnClickListener { this.loanChecked = loan }
            admin_zone_loansContent_loans_list.addView(loanRadio)

        }

    }

    override fun initLoan() {

        if(reserveChecked == null){
            toastL(this, "Primero selecctiona una Reserva por favor.")
        } else {

            val reserveDialog = ConfirmDialogActivity
                .Buider()
                .setTitleText("Confirmar Reserva")
                .setDescriptionText(
                    "Está a punto de iniciar el préstamo del recurso ${reserveChecked?.resourceName} con ISBN ${reserveChecked?.resourceId}. " +
                            "Informa al usuario de que el dispone de 5(Cinco) días hábiles para devolver el recurso, periodo tras el cual se le aplicará una sanción. " +
                            "\nSi el usuario precisa del recurso más tiempo deberá realizar una ampliación de préstamo." +
                            "\n ¿Desea confirmar el Préstamo?."
                )
                .setAcceptButtonText(getString(R.string.BTN_CONFIRM))
                .setCancelButtonText(getString(R.string.BTN_CANCEL))
                .buid()

            reserveDialog.show(supportFragmentManager!!, "InitLoanDialog")
            reserveDialog.isCancelable = false
            reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
                override fun clickAcceptButton() {
                    reserveDialog.dismiss()
                    adminPresenter.initLoan(reserveChecked!!)
                }

                override fun clickCancelButton() {
                    reserveDialog.dismiss()
                }

            })

        }

    }

    override fun cancelReserve() {

        if(reserveChecked == null){
            toastL(this, "Primero selecctiona una Reserva por favor.")
        } else {

            val reserveDialog = ConfirmDialogActivity
                .Buider()
                .setTitleText("Confirmar Reserva")
                .setDescriptionText(
                    "Está a punto de cancelar la reserva del recurso ${reserveChecked?.resourceName} con ISBN ${reserveChecked?.resourceId}. " +
                            "Informa al usuario de que el recurso volverá a estar dispobnible para otros usuarios y que deberá realizar una nueva reserva " +
                            "en caso de querer adquirir en préstamo el recurso." +
                            "\n ¿Desea continuar?."
                )
                .setAcceptButtonText(getString(R.string.BTN_YES))
                .setCancelButtonText(getString(R.string.BTN_NO))
                .buid()

            reserveDialog.show(supportFragmentManager!!, "CancelReserveDialog")
            reserveDialog.isCancelable = false
            reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
                override fun clickAcceptButton() {
                    reserveDialog.dismiss()
                    adminPresenter.cancelReserve(reserveChecked!!)
                }

                override fun clickCancelButton() {
                    reserveDialog.dismiss()
                }

            })

        }

    }

    override fun enlargeLoan() {

        if(loanChecked == null){
            toastL(this, "Primero selecctiona un Préstamo por favor.")
        } else {

            val reserveDialog = ConfirmDialogActivity
                .Buider()
                .setTitleText("Confirmar Reserva")
                .setDescriptionText(
                    "Está a punto de ampliar el préstamo del recurso ${loanChecked?.resourceName} con ISBN ${loanChecked?.resourceId}. " +
                            "Informa al usuario de que dispondrá de 5(Cinco) días hábiles adicionales a partir de hoy para devolver el recurso, periodo tras el cual " +
                            "se le aplicará una sanción. " +
                            "\nSi el usuario precisa del recurso más tiempo deberá realizar una ampliación de préstamo." +
                            "\n ¿Desea confirmar la ampliación?."
                )
                .setAcceptButtonText(getString(R.string.BTN_CONFIRM))
                .setCancelButtonText(getString(R.string.BTN_CANCEL))
                .buid()

            reserveDialog.show(supportFragmentManager!!, "EnlargeLoanDialog")
            reserveDialog.isCancelable = false
            reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
                override fun clickAcceptButton() {
                    reserveDialog.dismiss()
                    adminPresenter.enlargeLoan(loanChecked!!)
                }

                override fun clickCancelButton() {
                    reserveDialog.dismiss()
                }

            })

        }

    }


    override fun finalizeLoan() {

        if(loanChecked == null){
            toastL(this, "Primero selecctiona un Préstamo por favor.")
        } else {

            val reserveDialog = ConfirmDialogActivity
                .Buider()
                .setTitleText("Confirmar Reserva")
                .setDescriptionText(
                    "Está a punto de finalizar el préstamo del recurso ${reserveChecked?.resourceName} con ISBN ${reserveChecked?.resourceId}. " +
                            "Informa al usuario de que el recurso volverá a estar dispobnible para otros usuarios y que deberá realizar una nueva reserva " +
                            "en caso de querer adquirir en préstamo el recurso." +
                            "\nSi el usuario precisa del recurso más tiempo aún puede realizar una ampliación de préstamo." +
                            "\n ¿Desea continuar?."
                )
                .setAcceptButtonText(getString(R.string.BTN_YES))
                .setCancelButtonText(getString(R.string.BTN_NO))
                .buid()

            reserveDialog.show(supportFragmentManager!!, "FinalizeLoanDialog")
            reserveDialog.isCancelable = false
            reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
                override fun clickAcceptButton() {
                    reserveDialog.dismiss()
                    adminPresenter.finalizeLoan(loanChecked!!)
                }

                override fun clickCancelButton() {
                    reserveDialog.dismiss()
                }

            })

        }

    }

    // WINDOW METHODS
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
