package com.sinergia.eLibrary.presentation.UploadArticle.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel
import com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog.ConfirmDialogActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Neurolinguistics.View.NeurolinguisticsActivity
import com.sinergia.eLibrary.presentation.UploadArticle.Model.UploadArticleViewModel
import com.sinergia.eLibrary.presentation.UploadArticle.Model.UploadArticleViewModelImpl
import com.sinergia.eLibrary.presentation.UploadArticle.Presenter.UploadArticlePresenter
import com.sinergia.eLibrary.presentation.UploadArticle.UploadArticleContract
import kotlinx.android.synthetic.main.activity_upload_article.*
import kotlinx.android.synthetic.main.layout_headder_bar.*
import java.sql.DatabaseMetaData
import java.time.LocalDateTime

class UploadArticleActivity : BaseActivity(), UploadArticleContract.UploadArticleView {

    private lateinit var uploadArticlePresenter: UploadArticleContract.UploadArticlePresenter
    private lateinit var uploadArticleViewModel: UploadArticleViewModel

    var selectedCategory: String =  "Desconcido"
    private lateinit var newArticle: Article

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        uploadArticlePresenter = UploadArticlePresenter(UploadArticleViewModelImpl())
        uploadArticlePresenter.attachView(this)
        uploadArticleViewModel = UploadArticleViewModelImpl()

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        upload_article_btn.setOnClickListener { uploadArticle() }

        initCategories()

    }

    override fun getLayout(): Int {
        return R.layout.activity_upload_article
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_UPLOAD_ARTICLE)
    }

    // VIEW METHODS
    override fun showError(error: String) {
        toastL(this, error)
    }

    override fun showError(error: Int) {
        toastL(this, getString(error))
    }

    override fun showMessage(message: String) {
        toastL(this, message)
    }

    override fun showMessage(message: Int) {
        toastL(this, getString(message))
    }

    override fun showUploadArticleProgressBar() {
        upload_article_progressBar.visibility = View.VISIBLE
    }

    override fun hideUploadArticleProgressBar() {
        upload_article_progressBar.visibility = View.GONE
    }

    override fun enableUploadArticleButton() {
        upload_article_btn.isClickable = true
        upload_article_btn.isEnabled = true
    }

    override fun disableUploadArticleButton() {
        upload_article_btn.isClickable = false
        upload_article_btn.isEnabled = false
    }

    override fun initCategories() {
        for(category in NeLSProject.ARTICLE_CATEGORIES){
            var radioCategory = RadioButton(this)
            radioCategory.text = category
            radioCategory.setOnClickListener { selectedCategory = category }
            upload_article_category.addView(radioCategory)
        }
    }

    override fun uploadArticle() {

        var articleISSN = upload_article_ISSN.text.toString()
        var articleTitle = upload_article_title.text.toString()
        var articleAuthors = arrayListOf<String>()
        var articleEdition = -1
        if(!upload_article_edition.text.toString().isNullOrEmpty()) articleEdition =  Integer.parseInt(upload_article_edition.text.toString())
        var articleSource = upload_article_source.text.toString()
        var articleDesciption = upload_article_description.text.toString()

        if(upload_article_authors.text.toString().length > 0){
            for(author in upload_article_authors.text.toString().split(";")){
                articleAuthors.add(author)
            }
        }


        if(uploadArticlePresenter.checkEmptyFileds(articleTitle, articleAuthors, articleEdition, articleSource, articleDesciption)){

            if(uploadArticlePresenter.checkEmptyArticleTitle(articleTitle)) {
                upload_article_title.error = "El campo 'Título' es obligatorio."
            }
            if(uploadArticlePresenter.checkEmptyArticleAuthors(articleAuthors)){
                upload_article_authors.error = "El campo 'Autores' es obligatorio."
            }
            if(uploadArticlePresenter.checkEmptyArticleEdition(articleEdition)){
                upload_article_edition.error = "El campo 'Edición' es obligatorio."
            }
            if(uploadArticlePresenter.checkEmptyArticleSource(articleSource)){
                upload_article_source.error = "El campo 'Fuente' es obligatorio."
            }
            if(uploadArticlePresenter.checkEmptyArticleDescrption(articleDesciption)){
                upload_article_description.error = "El campo 'Descripción' es obligatorio."
            }

        } else {

            val articleId = "${LocalDateTime.now().toString()}-${NeLSProject.currentUser.email}"

            var article = Article(
                articleTitle,
                articleAuthors,
                articleEdition,
                articleSource,
                articleISSN,
                articleDesciption,
                selectedCategory,
                "Desconocido",
                articleId
            )

            newArticle = article

            if(uploadArticlePresenter.checkEmptyArticleISSN(articleISSN)){

                val reserveDialog = ConfirmDialogActivity
                    .Buider()
                    .setTitleText("ISSN no informado")
                    .setDescriptionText(
                        "AVISO: Está a punto de subir un artículo sin ISSN. Si el artículo aún no está publicado no se preocupe, " +
                            "puede modificar su ISSN posteriormente, en caso contrario, por favor, cumplimente este campo antes de continuar. " +
                                "\n ¿Desea continuar con la subida sin ISSN?."
                    )
                    .setAcceptButtonText(getString(R.string.BTN_YES))
                    .setCancelButtonText(getString(R.string.BTN_NO))
                    .buid()

                reserveDialog.show(supportFragmentManager!!, "ReserveDialog")
                reserveDialog.isCancelable = false
                reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
                    override fun clickAcceptButton() {
                        reserveDialog.dismiss()
                        uploadArticleStorage()
                    }

                    override fun clickCancelButton() {
                        reserveDialog.dismiss()
                    }

                })

            } else {
                uploadArticleStorage()
            }

        }


    }

    // STORAGE METHODS
    override fun uploadArticleStorage() {

        if(NeLSProject.storagePermissionGranted){
            val storageIntent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            storageIntent.type = "application/pdf"
            startActivityForResult(storageIntent, NeLSProject.GALLERY_INTENT_CODE)
        } else {
            toastL(this, "Por favor permite que la app acceda al almacenamiento del dispositivo.")
            checkAndSetStoragePermissions()
        }

    }

    // ACTICITY RESULTS METHODS
    override fun checkAndSetStoragePermissions() {
        val permissionStatusRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if(permissionStatusRead == PackageManager.PERMISSION_GRANTED) {
            NeLSProject.storagePermissionGranted = true
        } else {
            if (permissionStatusRead != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE), NeLSProject.READ_STORAGE_PERMISSIONS_CODE)
        }
    }

    override fun navigateToNeurolinguistics() {
        val intentNeurolinguistics = Intent(this, NeurolinguisticsActivity::class.java)
        intentNeurolinguistics.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentNeurolinguistics)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, fileData: Intent?) {

        super.onActivityResult(requestCode, resultCode, fileData)

        if(requestCode == NeLSProject.GALLERY_INTENT_CODE && resultCode == Activity.RESULT_OK){

            var fileURI: Uri = fileData?.data!!
            uploadArticlePresenter?.uploadArticle(newArticle, fileURI)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            NeLSProject.READ_STORAGE_PERMISSIONS_CODE ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    NeLSProject.storagePermissionGranted = (
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                } else {
                    toastL(this, "No se podrá acceder al almacenamiento del dispositivo hasta que concedas todos los permisos.")
                }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        uploadArticlePresenter.dettachView()
        uploadArticlePresenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        uploadArticlePresenter.dettachView()
        uploadArticlePresenter.dettachJob()
    }

}
