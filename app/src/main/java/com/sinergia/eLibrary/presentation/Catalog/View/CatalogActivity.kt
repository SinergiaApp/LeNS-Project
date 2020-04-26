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
import kotlinx.android.synthetic.main.layout_headder_bar.*
import kotlin.collections.ArrayList


class CatalogActivity: BaseActivity(), CatalogContract.CatalogView {

    private lateinit var catalogPresenter: CatalogContract.CatalogPresenter
    private lateinit var catalogViewModel: CatalogViewModel
    private var cameraPermissionGranted = false
    private var buttonRequestCameraPermission = false

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

        checkAndSetCamentaPermissions()
        catalog_camera_btn.setOnClickListener {

            if(!cameraPermissionGranted){
                toastL(this@CatalogActivity, "Por favor permite que la app acceda a la cámara")
                buttonRequestCameraPermission = true
                checkAndSetCamentaPermissions()
            } else {
                startScan()
            }

        }


        catalogPresenter.getAllResourcesToCatalog()

    }

    override fun getLayout(): Int {
        return R.layout.activity_catalog
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_CATALOG)
    }

    // CAMERA METHODS
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
                    cameraPermissionGranted = true
                } else {
                    toastL(this, "El escaneo no se podrá llevar a cabo hasta que no concedas los permisos de usar la cámara."
                    )
                }
        }
    }

    override fun startScan() {
        val scanIntent = Intent(this, CameraScanActivity::class.java)
        startActivityForResult(scanIntent, NeLSProject.CAMERA_INTENT_CODE)
    }

    override fun checkAndSetCamentaPermissions() {

        val permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)

        if(permissionStatus == PackageManager.PERMISSION_GRANTED ) {
            cameraPermissionGranted = true
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), NeLSProject.CAMERA_PERMISSIONS_CODE)
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

            for (book in resourcesList) {
                val resource = LinearLayout(this)
                var params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                    weight = LinearLayout.LayoutParams.MATCH_PARENT.toFloat()
                    height = LinearLayout.LayoutParams.MATCH_PARENT
                    bottomMargin = 100
                }
                resource.orientation = LinearLayout.HORIZONTAL
                resource.setBackground(ContextCompat.getDrawable(this, R.drawable.style_list_resource))
                resource.layoutParams = params
                resource.setPadding(20,10,20,10)
                val description = LinearLayout(this)
                params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
                ).apply {
                    gravity = Gravity.CENTER_VERTICAL
                    weight = LinearLayout.LayoutParams.MATCH_PARENT.toFloat()
                    height = LinearLayout.LayoutParams.MATCH_PARENT
                }
                description.setOrientation(LinearLayout.VERTICAL)
                description.layoutParams = params


                val imageLayout = RelativeLayout(this)
                val image = ImageView(this)
                image.setImageResource(R.drawable.icon_logonels)
                imageLayout.addView(image)
                val layoutParams = LinearLayout.LayoutParams(250, 250)
                imageLayout.setLayoutParams(layoutParams)

                //Título del Libro
                val title = TextView(this)
                val titletxt = book.title
                title.setText("Título: $titletxt.")
                title.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                //Autor del Libro
                val author = TextView(this)
                val authortxt = book.author
                author.setText("Autor: $authortxt.")
                author.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                //Editorial del Libro
                val publisher = TextView(this)
                val publishertxt = book.publisher
                publisher.setText("Editorial: $publishertxt.")
                publisher.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                //Edición del Libro
                val edition = TextView(this)
                val editiontxt = book.edition
                edition.setText("Edicion: $editiontxt.")
                edition.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
                //ISBN del Libro
                val isbn = TextView(this)
                val isbntxt =book.isbn
                isbn.setText("ISBN: $isbntxt.")
                isbn.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))


                description.addView(title)
                description.addView(author)
                description.addView(publisher)
                description.addView(edition)
                description.addView(isbn)

                resource.addView(imageLayout)
                resource.addView(description)
                resource.setOnClickListener {
                    navigateToBook(book)
                }
                catalog_content.addView(resource)
            }

        }
    }

    override fun initCatalog(book: Resource?) {
        if(book == null){
            toastL(this, "Vaya... Parece que no hay ningún recurso en la Base de Datos con esos parámetros...")
        } else {

            val resource = LinearLayout(this)
            var params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
                weight = LinearLayout.LayoutParams.MATCH_PARENT.toFloat()
                height = LinearLayout.LayoutParams.MATCH_PARENT
                bottomMargin = 100
            }
            resource.orientation = LinearLayout.HORIZONTAL
            resource.setBackground(ContextCompat.getDrawable(this, R.drawable.style_list_resource))
            resource.layoutParams = params
            resource.setPadding(20, 10, 20, 10)
            val description = LinearLayout(this)
            params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
                weight = LinearLayout.LayoutParams.MATCH_PARENT.toFloat()
                height = LinearLayout.LayoutParams.MATCH_PARENT
            }
            description.setOrientation(LinearLayout.VERTICAL)
            description.layoutParams = params


            val imageLayout = RelativeLayout(this)
            val image = ImageView(this)
            image.setImageResource(R.drawable.icon_logonels)
            params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            imageLayout.setLayoutParams(params)

            val likeIcon = ImageView(this)
            likeIcon.setImageResource(R.drawable.icon_logonels)
            var layoutParams = LinearLayout.LayoutParams(25, 25)
            likeIcon.setLayoutParams(layoutParams)
            likeIcon.setOnClickListener { catalogPresenter.setLikes(book, FirebaseAuth.getInstance().currentUser?.email!!, 1) }
            val likes = TextView(this)
            val likesNumber = book.likes.size
            likes.setText(likesNumber)
            likes.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            val dislikeIcon = ImageView(this)
            dislikeIcon.setImageResource(R.drawable.icon_logonels)
            layoutParams = LinearLayout.LayoutParams(25, 25)
            dislikeIcon.setLayoutParams(layoutParams)
            likeIcon.setOnClickListener { catalogPresenter.setLikes(book, FirebaseAuth.getInstance().currentUser?.email!!, 1) }
            val disLikes = TextView(this)
            val disLikesNumber = book.dislikes.size
            disLikes.setText(disLikesNumber)
            disLikes.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))



            val iconsLayout = LinearLayout(this)
            iconsLayout.orientation = LinearLayout.HORIZONTAL
            params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            iconsLayout.layoutParams = params
            iconsLayout.addView(likes)
            iconsLayout.addView(likeIcon)
            iconsLayout.addView(disLikes)
            iconsLayout.addView(dislikeIcon)



            val imageIconsLayout = LinearLayout(this)
            imageIconsLayout.orientation = LinearLayout.VERTICAL
            params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            imageIconsLayout.layoutParams = params
            imageIconsLayout.addView(imageLayout)
            imageIconsLayout.addView(iconsLayout)

            //Título del Libro
            val title = TextView(this)
            val titletxt = book.title
            title.setText("Título: $titletxt.")
            title.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //Autor del Libro
            val author = TextView(this)
            val authortxt = book.author
            author.setText("Autor: $authortxt.")
            author.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //Editorial del Libro
            val publisher = TextView(this)
            val publishertxt = book.publisher
            publisher.setText("Editorial: $publishertxt.")
            publisher.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //Edición del Libro
            val edition = TextView(this)
            val editiontxt = book.edition
            edition.setText("Edicion: $editiontxt.")
            edition.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //ISBN del Libro
            val isbn = TextView(this)
            val isbntxt = book.isbn
            isbn.setText("ISBN: $isbntxt.")
            isbn.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))


            description.addView(title)
            description.addView(author)
            description.addView(publisher)
            description.addView(edition)
            description.addView(isbn)

            resource.addView(imageIconsLayout)
            resource.addView(description)
            resource.setOnClickListener {
                navigateToBook(book)
            }
            catalog_content.addView(resource)
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
    }

    override fun onDestroy() {
        super.onDestroy()
        catalogPresenter.dettachView()
    }

}

