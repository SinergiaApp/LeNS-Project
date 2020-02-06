package com.sinergia.eLibrary.presentation.Catalog.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.ViewModelProviders
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsDataBase
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModelImpl
import com.sinergia.eLibrary.presentation.Catalog.Presenter.ItemCatalogPresenter
import com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog.ConfirmDialog
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.android.synthetic.main.activity_item_catalog.*
import kotlinx.android.synthetic.main.activity_item_catalog.menu_button
import com.sinergia.eLibrary.data.Model.Resource as Resource

class ItemCatalogActivity : BaseActivity(), ItemCatalogContract.ItemCatalogView {

    private lateinit var itemCatalogPresenter: ItemCatalogContract.ItemCatalogPresenter
    private lateinit var itemCatalogViewModel: ItemCatalogViewModel
    private var libraryChecked: String ?= null
    private var currentResource: Resource ?= null


    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemCatalogPresenter = ItemCatalogPresenter(ItemCatalogViewModelImpl())
        itemCatalogPresenter.attachView(this)
        itemCatalogViewModel = ViewModelProviders.of(this).get(ItemCatalogViewModelImpl::class.java)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        itemCatalogPresenter.getItemCatalog(NeLSProject.book)

        item_catalog_like_btn.setOnClickListener { setLikes() }
        item_catalog_dislike_btn.setOnClickListener { setDislikes() }
        item_catalog_reserve_btn.setOnClickListener { reserveResource() }

    }

    override fun getLayout(): Int {
        return R.layout.activity_item_catalog
    }

    override fun getPageTitle(): String {
        return NeLSProject.book
    }

    //ITEM CATALOG CONTRACT METHODS
    override fun showMessage(message: String?) {
        toastL(this, message)
    }

    override fun showError(errorMsg: String?) {
        toastL(this, errorMsg)
    }

    override fun showItemCatalogProgressBar() {
        item_catalog_progressBar.visibility = View.VISIBLE
    }

    override fun hideItemCatalogProgressBar() {
        item_catalog_progressBar.visibility = View.GONE
    }

    override fun showItemCatalogContent() {
        item_catalog_content.visibility = View.VISIBLE
    }

    override fun hideItemCatalogContent() {
        item_catalog_content.visibility = View.GONE
    }

    override fun showHideDisponibilityContent() {
        if(item_catalog_disponibility_content.visibility == View.VISIBLE){
            item_catalog_disponibility_content.visibility = View.GONE
        }else {
            item_catalog_disponibility_content.visibility =View.VISIBLE
        }
    }

    override fun enableDisponibilityButtom() {
        item_catalog_disponibility_btn.isEnabled = true
        item_catalog_disponibility_btn.isClickable = true
        item_catalog_disponibility_btn.setOnClickListener { showHideDisponibilityContent() }
    }

    override fun disableDisponibilityButtom() {
        item_catalog_disponibility_btn.isEnabled = false
        item_catalog_disponibility_btn.isClickable = true
        item_catalog_disponibility_btn.setOnClickListener { toastL(this, "Recurso no disponible para préstamo actualmente.") }
    }

    override fun enableOnLineButton(urlOnline: String) {
        item_catalog_onLine_btn.isEnabled = true
        item_catalog_onLine_btn.isClickable = true
        item_catalog_onLine_btn.setOnClickListener { goToOnline(urlOnline) }
    }

    override fun disableOnLineButton() {
        item_catalog_onLine_btn.isEnabled = false
        item_catalog_onLine_btn.isClickable = true
        item_catalog_onLine_btn.setOnClickListener { toastL(this, "Recurso no disponible para visionado online.") }
    }

    override fun goToOnline(urlOnline: String) {
        if (!urlOnline.startsWith("http://") && !urlOnline.startsWith("https://")){
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://$urlOnline")))
        } else {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlOnline)))
        }

    }

    override fun reserveResource() {
        if(!this.libraryChecked.isNullOrEmpty()){
            toastS(this, "Primero selecctiona una Biblioteca por vafor.")
        } else {

            val reserveDialog = ConfirmDialog
                .Buider()
                .setTitleText("Confirmar Reserva")
                .setDescriptionText(
                    "Está a punto de confirmar la reserva y comenzar el proceso de préstamo del " +
                    "recurso. Una vez confirme la reserva dispondrá de un total de 2 (dos) días " +
                    "hábiles para ir a la biblioteca seleccionada y recoger el recurso. En caso " +
                    "de que este tiempo expire, la reserva se cancelará automáticamente y el " +
                    "libro pasará a estar disponible para reservar al resto de usuarios de nuevo." +
                    " ¿Desea confirmar la Reserva?."
                )
                .setAcceptButtonText("ACEPTAR")
                .setCancelButtonText("CANCELAR")
                .buid()

            reserveDialog.show(supportFragmentManager!!, "ReserveDialog")

        }
    }


    override fun initItemCatalogContent(resource: Resource?) {

        this.currentResource = resource
        item_catalog_title.text = resource?.title
        item_catalog_isbn.text = "ISBN: \n" + resource?.isbn
        item_catalog_author.text = "Autor: \n" + resource?.author
        item_catalog_publisher.text = "Editorioal: \n" + resource?.publisher
        item_catalog_edition.text = "Edición: \n" + resource?.edition
        item_catalog_sinopsis.text = "Sinopsis: \n" + resource?.sinopsis
        item_catalog_likes.text = resource?.likes?.size.toString()
        item_catalog_dislikes.text = resource?.dislikes?.size.toString()

        if(resource?.isOnline!!){
            enableOnLineButton(resource.urlOnline)
        } else {
            disableOnLineButton()
        }

        if(!resource?.disponibility.isNullOrEmpty()){

            enableDisponibilityButtom()

            for(key in resource?.disponibility.keys){

                if(Integer(resource?.disponibility.get(key).toString()) < 0 || resource?.disponibility.get(key) != null) {
                    var library = RadioButton(this)
                    library.text = resource?.disponibility.get(key).toString()
                    library.setOnClickListener { this.libraryChecked = library.text.toString() }
                    item_catalog_disponibility_radio.addView(library)
                }

            }

        } else {
            disableDisponibilityButtom()
        }

    }
    override fun setLikes() {
        if(itemCatalogPresenter.chekRepeatLikeDislike(currentResource?.likes!!)){
            toastL(this, "Ya has indicado que te gusta este libro.")
        } else {
            val newLikesList = currentResource!!.likes
            val newDislikesList = currentResource!!.dislikes
            newLikesList.add(NeLSProject.currentUser.email)
            newDislikesList.remove(NeLSProject.currentUser.email)
            val modifiedResource = Resource(
                currentResource!!.title,
                currentResource!!.author,
                currentResource!!.publisher,
                currentResource!!.edition,
                currentResource!!.sinopsis,
                currentResource!!.isbn,
                currentResource!!.disponibility,
                newLikesList,
                newDislikesList,
                currentResource!!.isOnline,
                currentResource!!.urlOnline
            )
            itemCatalogPresenter.setResourceLikes(modifiedResource)
        }
    }

    override fun setDislikes() {
        if(itemCatalogPresenter.chekRepeatLikeDislike(currentResource?.dislikes!!)){
            toastL(this, "Ya has indicado que no te gusta este libro.")
        } else {
            val newLikesList = currentResource!!.likes
            val newDislikesList = currentResource!!.dislikes
            newLikesList.remove(NeLSProject.currentUser.email)
            newDislikesList.add(NeLSProject.currentUser.email)
            val modifiedResource = Resource(
                currentResource!!.title,
                currentResource!!.author,
                currentResource!!.publisher,
                currentResource!!.edition,
                currentResource!!.sinopsis,
                currentResource!!.isbn,
                currentResource!!.disponibility,
                newLikesList,
                newDislikesList,
                currentResource!!.isOnline,
                currentResource!!.urlOnline
            )
            itemCatalogPresenter.setResourceLikes(modifiedResource)
        }
    }

}
