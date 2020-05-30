package com.sinergia.eLibrary.presentation.Neurolinguistics.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.base.utils.CreateCards
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Neurolinguistics.Model.NeurolinguisticsViewModelImpl
import com.sinergia.eLibrary.presentation.Neurolinguistics.NeurolinguisticsContract
import com.sinergia.eLibrary.presentation.Neurolinguistics.Presenter.NeurolinguisticsPresenter
import kotlinx.android.synthetic.main.activity_neurolinguistics.*
import kotlinx.android.synthetic.main.layout_headder_bar.*

class NeurolinguisticsActivity : BaseActivity(), NeurolinguisticsContract.NeurolinguisticsView {

    lateinit var articlesList: ArrayList<Article>

    private val createCards = CreateCards()
    lateinit var neuroPresenter: NeurolinguisticsContract.NeurolinguisticsPresenter


    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        neuroPresenter = NeurolinguisticsPresenter(NeurolinguisticsViewModelImpl())
        neuroPresenter.attachView(this)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        neuro_search_btn.setOnClickListener{ search() }
        neuro_upload_btn.setOnClickListener { uploadArticle() }

        neuroPresenter.getAllArticlesToCatalog()

    }

    override fun getLayout(): Int {
        return R.layout.activity_neurolinguistics
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_NEUROLINGUISTICS)
    }

    //VIEW METHODS
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

    override fun showNeuroProgressBar() {
        neuro_progressBar.visibility = View.VISIBLE
    }

    override fun hideNeuroProgressBar() {
        neuro_progressBar.visibility = View.GONE
    }

    override fun showContent() {
        neuro_content.visibility = View.VISIBLE
    }

    override fun hideContent() {
        neuro_content.visibility = View.GONE
    }

    override fun enableUploadButton() {
        neuro_upload_btn.isClickable = true
        neuro_upload_btn.isEnabled = true
    }

    override fun disableUploadButton() {
        neuro_upload_btn.isClickable = false
        neuro_upload_btn.isEnabled = false
    }

    override fun initCatalog(articlesList: ArrayList<Article>?) {

        for(article in articlesList!!){

            eraseCatalog()
            var articleCard = createCards.createArticleCard(this, article)
            articleCard.setOnClickListener { navigateToArticle(article) }
            neuro_content.addView(articleCard)

        }

    }

    override fun navigateToArticle(article: Article) {
        NeLSProject.currentArticle = article
        startActivity(Intent(this, ItemNeurolinguisticsActivity::class.java))
    }

    override fun eraseCatalog() {
        neuro_content.removeAllViews()
    }

    override fun search() {
        var searcher: String = neuro_search.text.toString()
        neuroPresenter.search(articlesList, searcher)
    }

    override fun uploadArticle() {
        TODO("Not yet implemented")
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        neuroPresenter.dettachView()
        neuroPresenter.dettachJob()
    }

    override fun onDestroy() {
        super.onDestroy()
        neuroPresenter.dettachView()
        neuroPresenter.dettachJob()
    }
}
