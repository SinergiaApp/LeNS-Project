package com.sinergia.eLibrary.presentation.Catalog.View

import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.android.synthetic.main.activity_item_catalog.*

class ItemCatalogActivity : BaseActivity(), ItemCatalogContract.ItemCatalogView {

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        page_title.text = getPageTitle()

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

    override fun initItemCatalogContent() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
