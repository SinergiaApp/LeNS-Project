package com.sinergia.eLibrary.presentation.Neurolinguistics.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Neurolinguistics.Model.NeurolinguisticsViewModelImpl
import com.sinergia.eLibrary.presentation.Neurolinguistics.NeurolinguisticsContract
import com.sinergia.eLibrary.presentation.Neurolinguistics.Presenter.ItemNeurolinguisticsPresenter
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
        item_neuro_set_btn.setOnClickListener {  }
        item_neuro_delete_btn.setOnClickListener { deleteArticle() }

        initContent()

    }

    override fun getLayout(): Int {
        return R.layout.activity_item_neurolinguistics
    }

    override fun getPageTitle(): String {
        return NeLSProject.currentArticle!!.title
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

        val title = "${item_neuro_title.text}\n\t ${NeLSProject.currentArticle!!.title}"
        val issn = "${item_neuro_title.text}\n\t ${NeLSProject.currentArticle!!.issn}"
        val authors = "${item_neuro_title.text}\n\t ${NeLSProject.currentArticle!!.authors}"
        val source = "${item_neuro_title.text}\n\t ${NeLSProject.currentArticle!!.source}"
        val edition = "${item_neuro_title.text}\n\t ${NeLSProject.currentArticle!!.year}"
        val description = "${item_neuro_title.text}\n\t ${NeLSProject.currentArticle!!.descriptiom}"

        item_neuro_title.text = title
        item_neuro_issn.text = issn
        item_neuro_authors.text = authors
        item_neuro_source.text = source
        item_neuro_edition.text = edition
        item_neuro_description.text = description

    }

    override fun downloadArticle() {
        itemNeuroPresenter.downloadArticle()
    }

    override fun setArticle() {
        TODO("Not yet implemented")
    }

    override fun deleteArticle() {
        itemNeuroPresenter.deleteArticle()
    }


}
