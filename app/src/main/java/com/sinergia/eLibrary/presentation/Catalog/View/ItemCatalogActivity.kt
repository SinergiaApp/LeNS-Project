package com.sinergia.eLibrary.presentation.Catalog.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModelImpl
import com.sinergia.eLibrary.presentation.Catalog.Presenter.ItemCatalogPresenter
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.android.synthetic.main.activity_catalog.*
import kotlinx.android.synthetic.main.activity_item_catalog.*
import kotlinx.android.synthetic.main.activity_item_catalog.menu_button
import com.sinergia.eLibrary.data.Model.Resource as Resource

class ItemCatalogActivity : BaseActivity(), ItemCatalogContract.ItemCatalogView {

    private lateinit var itemCatalogPresenter: ItemCatalogContract.ItemCatalogPresenter
    private lateinit var itemCatalogViewModel: ItemCatalogViewModel



    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemCatalogPresenter = ItemCatalogPresenter(ItemCatalogViewModelImpl())
        itemCatalogPresenter.attachView(this)
        itemCatalogViewModel = ViewModelProviders.of(this).get(ItemCatalogViewModelImpl::class.java)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        itemCatalogPresenter.getItemCatalog(NeLSProject.book)

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

    override fun initItemCatalogContent(resource: Resource?) {

        item_catalog_title.text = resource?.title
        item_catalog_isbn.text = "ISBN: \n" + resource?.isbn
        item_catalog_author.text = "Autor: \n" + resource?.author
        item_catalog_publisher.text = "Editorioal: \n" + resource?.publisher
        item_catalog_edition.text = "Edición: \n" + resource?.edition
        item_catalog_sinopsis.text = "Sinopsis: \n" + resource?.sinopsis

    }


}
