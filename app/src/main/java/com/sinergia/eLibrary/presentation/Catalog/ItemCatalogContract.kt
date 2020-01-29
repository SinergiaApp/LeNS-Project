package com.sinergia.eLibrary.presentation.Catalog

import com.sinergia.eLibrary.data.Model.Resource


interface ItemCatalogContract {

    interface ItemCatalogView {

        fun showMessage(message: String?)
        fun showError(errorMsg: String?)
        fun showItemCatalogProgressBar()
        fun hideItemCatalogProgressBar()
        fun showItemCatalogContent()
        fun hideItemCatalogContent()

        fun enableDisponibilityButtom()
        fun disableDisponibilityButtom()
        fun enableOnLineButton(urlOnline: String)
        fun disableOnLineButton()
        fun goToOnline(urlOnline: String)
        fun showHideDisponibilityContent()
        fun setLikes()
        fun setDislikes()
        fun reserveResource()

        fun initItemCatalogContent(resource: Resource?)

    }

    interface ItemCatalogPresenter {

        fun attachView(view: ItemCatalogView)
        fun dettachView()
        fun isViewAttached(): Boolean
        fun dettachJob()
        fun chekRepeatLikeDislike(list: MutableList<String>): Boolean

        fun getItemCatalog(isbn: String)
        fun setResourceLikes(resource: Resource)

    }

}