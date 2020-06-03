package com.sinergia.eLibrary.presentation.Neurolinguistics.View

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog.ConfirmDialogActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Neurolinguistics.Model.NeurolinguisticsViewModelImpl
import com.sinergia.eLibrary.presentation.Neurolinguistics.NeurolinguisticsContract
import com.sinergia.eLibrary.presentation.Neurolinguistics.Presenter.ItemNeurolinguisticsPresenter
import com.sinergia.eLibrary.presentation.SetArticle.View.SetArticleActivity
import kotlinx.android.synthetic.main.activity_item_neurolinguistics.*
import kotlinx.android.synthetic.main.layout_headder_bar.*

class ItemNeurolinguisticsActivity : BaseActivity(), NeurolinguisticsContract.ItemNeurolinguisticsView {

    lateinit var itemNeuroPresenter: NeurolinguisticsContract.ItemNeurolinguisticsPresenter

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        itemNeuroPresenter = ItemNeurolinguisticsPresenter(NeurolinguisticsViewModelImpl())
        itemNeuroPresenter.attachView(this)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        item_neuro_download_btn.setOnClickListener { downloadArticle() }
        item_neuro_set_btn.setOnClickListener { setArticle() }
        item_neuro_delete_btn.setOnClickListener { deleteArticle() }

        initContent()

    }

    override fun getLayout(): Int {
        return R.layout.activity_item_neurolinguistics
    }

    override fun getPageTitle(): String {
        return NeLSProject.currentArticle!!.title
    }

    override fun backButton() {
        if(NeLSProject.backButtonPressedTwice){
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("EXIT", true)
            startActivity(intent)
        } else {
            toastL(this, getString(R.string.BTN_BACK))
            NeLSProject.backButtonPressedTwice = true
        }
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

    override fun showItemNeuroProgressBar() {
        item_neuro_progressBar.visibility = View.VISIBLE
    }

    override fun hideItemNeuroProgressBar() {
        item_neuro_progressBar.visibility = View.GONE
    }

    override fun showUpdateAndDeleteButtons() {
        item_neuro_set_btn.visibility = View.VISIBLE
        item_neuro_delete_btn.visibility = View.VISIBLE
    }

    override fun enableButtons() {
        item_neuro_download_btn.isClickable = true
        item_neuro_download_btn.isEnabled = true
        item_neuro_set_btn.isClickable = true
        item_neuro_set_btn.isEnabled = true
        item_neuro_delete_btn.isClickable = true
        item_neuro_delete_btn.isEnabled = true
    }

    override fun disableButtons() {
        item_neuro_download_btn.isClickable = false
        item_neuro_download_btn.isEnabled = false
        item_neuro_set_btn.isClickable = false
        item_neuro_set_btn.isEnabled = false
        item_neuro_delete_btn.isClickable = false
        item_neuro_delete_btn.isEnabled = false
    }

    override fun initContent() {

        val title = "${item_neuro_title.text}\n\t ${NeLSProject.currentArticle!!.title}."
        val issn = "${item_neuro_issn.text}\n\t ${NeLSProject.currentArticle!!.issn}."
        val category = "${item_neuro_category.text}\n\t ${NeLSProject.currentArticle!!.category}."
        var authors = "${item_neuro_authors.text}\n\t"
        val source = "${item_neuro_source.text}\n\t ${NeLSProject.currentArticle!!.source}."
        val edition = "${item_neuro_edition.text}\n\t ${NeLSProject.currentArticle!!.year}."
        val description = "${item_neuro_description.text}\n\t ${NeLSProject.currentArticle!!.descriptiom}."

        for(author in NeLSProject.currentArticle!!.authors){
            authors += "$author, "
        }
        authors = authors.substring(0, authors.length-2)
        authors += "."

        item_neuro_title.text = title
        item_neuro_issn.text = issn
        item_neuro_category.text = category
        item_neuro_authors.text = authors
        item_neuro_source.text = source
        item_neuro_edition.text = edition
        item_neuro_description.text = description

        if(NeLSProject.currentArticle!!.owner == NeLSProject.currentUser.email) showUpdateAndDeleteButtons()

    }

    override fun downloadArticle() {
        //itemNeuroPresenter.downloadArticle()
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(NeLSProject.currentArticle!!.downloadURI)))
    }

    override fun setArticle() {
        val intentSetArticle = Intent(this, SetArticleActivity::class.java)
        intentSetArticle.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentSetArticle)
    }

    override fun deleteArticle() {

        val reserveDialog = ConfirmDialogActivity
            .Buider()
            .setTitleText("Confirmar Eliminación")
            .setDescriptionText(
                "AVISO: Está a punto de eliminar de forma permanente el artículo con título ${{NeLSProject.currentArticle!!.title}}" +
                        "\n ¿Confirma que desea eliminar dicho artículo?."
            )
            .setAcceptButtonText(getString(R.string.BTN_CONFIRM))
            .setCancelButtonText(getString(R.string.BTN_NO))
            .buid()

        reserveDialog.show(supportFragmentManager, "ReserveDialog")
        reserveDialog.isCancelable = false
        reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
            override fun clickAcceptButton() {
                reserveDialog.dismiss()
                itemNeuroPresenter.deleteArticle()
            }

            override fun clickCancelButton() {
                reserveDialog.dismiss()
            }

        })

    }

    override fun navigateToNeurolinguistics() {
        val intentNeurolinguistics = Intent(this, NeurolinguisticsActivity::class.java)
        intentNeurolinguistics.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentNeurolinguistics)
    }


}
