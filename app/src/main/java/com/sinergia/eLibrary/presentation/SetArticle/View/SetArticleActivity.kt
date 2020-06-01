package com.sinergia.eLibrary.presentation.SetArticle.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Article
import com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog.ConfirmDialogActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import com.sinergia.eLibrary.presentation.Neurolinguistics.View.NeurolinguisticsActivity
import com.sinergia.eLibrary.presentation.SetArticle.Model.SetArticleViewModel
import com.sinergia.eLibrary.presentation.SetArticle.Model.SetArticleViewModelImpl
import com.sinergia.eLibrary.presentation.SetArticle.Presenter.SetArticlePresenter
import com.sinergia.eLibrary.presentation.SetArticle.SetArticleContract
import kotlinx.android.synthetic.main.activity_set_article.*
import kotlinx.android.synthetic.main.layout_headder_bar.*

class SetArticleActivity : BaseActivity(), SetArticleContract.SetArticleView {

    private lateinit var setArticlePresenter: SetArticleContract.SetArticlePresenter
    private lateinit var setArticleViewModel: SetArticleViewModel

    private lateinit var selectedCategory: String

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setArticlePresenter = SetArticlePresenter(SetArticleViewModelImpl())
        setArticlePresenter.attachView(this)
        setArticleViewModel = SetArticleViewModelImpl()

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        set_article_btn.setOnClickListener { setArticle() }

        initContent()

    }

    override fun getLayout(): Int {
        return R.layout.activity_set_article
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_SET_ARTICLE)
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

    override fun showSetArticleProgressBar() {
        set_article_progressBar.visibility = View.VISIBLE
    }

    override fun hideSetArticleProgressBar() {
        set_article_progressBar.visibility = View.GONE
    }

    override fun enableSetArticleButton() {
        set_article_btn.isClickable = true
        set_article_btn.isEnabled = true
    }

    override fun disableSetdArticleButton() {
        set_article_btn.isClickable = false
        set_article_btn.isEnabled = false
    }

    override fun initContent() {

        set_article_ISSN.setText(NeLSProject.currentArticle?.issn)
        set_article_title.setText(NeLSProject.currentArticle?.title)
        set_article_edition.setText(NeLSProject.currentArticle?.year.toString())
        set_article_source.setText(NeLSProject.currentArticle?.source)
        set_article_description.setText(NeLSProject.currentArticle?.descriptiom)

        var articleAuthors = ""
        for(author in NeLSProject.currentArticle!!.authors){
            articleAuthors += "$author;"
        }
        articleAuthors = articleAuthors.substring(0, articleAuthors.length-2)

        set_article_authors.setText(articleAuthors)

        for(category in NeLSProject.ARTICLE_CATEGORIES){
            var radioCategory = RadioButton(this)
            radioCategory.setText(category)
            if(category == NeLSProject.currentArticle!!.category) radioCategory.isSelected = true
            radioCategory.setOnClickListener { selectedCategory = category }
            set_article_category.addView(radioCategory)
        }
    }

    override fun setArticle() {

        var articleISSN = set_article_ISSN.text.toString()
        var articleTitle = set_article_title.text.toString()
        var articleAuthors = arrayListOf<String>()
        var articleEdition = Integer.parseInt(set_article_edition.text.toString())
        var articleSource = set_article_source.text.toString()
        var articleDesciption = set_article_description.text.toString()

        for(author in set_article_authors.text.toString().split(";")){
            articleAuthors.add(author)
        }

        if(setArticlePresenter.checkEmptyFileds(articleTitle, articleAuthors, articleEdition, articleSource, articleDesciption)){

            if(setArticlePresenter.checkEmptyArticleTitle(articleTitle)) {
                set_article_title.error = "El campo 'Título' es obligatorio."
            }
            if(setArticlePresenter.checkEmptyArticleAuthors(articleAuthors)){
                set_article_authors.error = "El campo 'Autores' es obligatorio."
            }
            if(setArticlePresenter.checkEmptyArticleEdition(articleEdition)){
                set_article_edition.error = "El campo 'Edición' es obligatorio."
            }
            if(setArticlePresenter.checkEmptyArticleSource(articleSource)){
                set_article_source.error = "El campo 'Fuente' es obligatorio."
            }
            if(setArticlePresenter.checkEmptyArticleDescrption(articleDesciption)){
                set_article_description.error = "El campo 'Descripción' es obligatorio."
            }

        } else {

            var settedArticle = Article(
                articleTitle,
                articleAuthors,
                articleEdition,
                articleSource,
                articleISSN,
                articleDesciption,
                selectedCategory,
                NeLSProject.currentUser.email,
                NeLSProject.currentArticle!!.downloadURI,
                NeLSProject.currentArticle!!.id
            )

            if(setArticlePresenter.checkEmptyArticleISSN(articleISSN)){

                val reserveDialog = ConfirmDialogActivity
                    .Buider()
                    .setTitleText("Confirmar Eliminación Permanente de Cuenta")
                    .setDescriptionText(
                        "AVISO: \n Está a punto de subir un artículo sin ISSN, ¿Desea continuar?."
                    )
                    .setAcceptButtonText(getString(R.string.BTN_YES))
                    .setCancelButtonText(getString(R.string.BTN_NO))
                    .buid()

                reserveDialog.show(supportFragmentManager, "ReserveDialog")
                reserveDialog.isCancelable = false
                reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
                    override fun clickAcceptButton() {
                        reserveDialog.dismiss()
                        setArticlePresenter.setArticle(settedArticle)
                    }

                    override fun clickCancelButton() {
                        reserveDialog.dismiss()
                    }

                })

            } else {
                setArticlePresenter.setArticle(settedArticle)
            }

        }


    }

    override fun navigateToNeurolinguistics() {
        val intentNeurolinguistics = Intent(this, NeurolinguisticsActivity::class.java)
        intentNeurolinguistics.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentNeurolinguistics)
    }
}
