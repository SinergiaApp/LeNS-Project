package com.sinergia.eLibrary.presentation.Catalog.View

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import kotlinx.android.synthetic.main.activity_catalog.*
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.google.common.base.Strings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthSettings
import com.google.firebase.auth.FirebaseUser
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModelImpl
import com.sinergia.eLibrary.presentation.Catalog.Presenter.CatalogPresenter
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import java.util.*
import kotlin.collections.ArrayList


class CatalogActivity: BaseActivity(), CatalogContract.CatalogView {

    private lateinit var catalogPresenter: CatalogContract.CatalogPresenter
    private lateinit var catalogViewModel: CatalogViewModel

    //ACTIVITY TITLE
    override fun getPageTitle(): String {
        return "CATÁLOGO"
    }


    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        catalogPresenter = CatalogPresenter(CatalogViewModelImpl())
        catalogPresenter.attachView(this)
        catalogViewModel = ViewModelProviders.of(this).get(CatalogViewModelImpl::class.java)

        main_page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }
        catalog_search_btn.setOnClickListener {
            if(Strings.isNullOrEmpty(catalog_search.text.toString())){
                catalogPresenter.getAllResourcesToCatalog()
            } else {
                catalogPresenter.getResourceToCatalog(catalog_search.text.toString())
            }
        }


        catalogPresenter.getAllResourcesToCatalog()
    }

    override fun getLayout(): Int {
        return R.layout.activity_catalog
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
                resource.setBackground(ContextCompat.getDrawable(this, R.drawable.list_resource_style))
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
                image.setImageResource(R.drawable.logonels)
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
                    navigateToBook(book.isbn)
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
            resource.setBackground(ContextCompat.getDrawable(this, R.drawable.list_resource_style))
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
            image.setImageResource(R.drawable.logonels)
            params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            imageLayout.setLayoutParams(params)

            val likeIcon = ImageView(this)
            likeIcon.setImageResource(R.drawable.logonels)
            var layoutParams = LinearLayout.LayoutParams(25, 25)
            likeIcon.setLayoutParams(layoutParams)
            likeIcon.setOnClickListener { catalogPresenter.setLikes(book, FirebaseAuth.getInstance().currentUser?.email!!, 1) }
            val likes = TextView(this)
            val likesNumber = book?.likes.size
            likes.setText(likesNumber)
            likes.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            val dislikeIcon = ImageView(this)
            dislikeIcon.setImageResource(R.drawable.logonels)
            layoutParams = LinearLayout.LayoutParams(25, 25)
            dislikeIcon.setLayoutParams(layoutParams)
            likeIcon.setOnClickListener { catalogPresenter.setLikes(book, FirebaseAuth.getInstance().currentUser?.email!!, 1) }
            val disLikes = TextView(this)
            val disLikesNumber = book?.dislikes.size
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
            val titletxt = book?.title
            title.setText("Título: $titletxt.")
            title.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //Autor del Libro
            val author = TextView(this)
            val authortxt = book?.author
            author.setText("Autor: $authortxt.")
            author.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //Editorial del Libro
            val publisher = TextView(this)
            val publishertxt = book?.publisher
            publisher.setText("Editorial: $publishertxt.")
            publisher.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //Edición del Libro
            val edition = TextView(this)
            val editiontxt = book?.edition
            edition.setText("Edicion: $editiontxt.")
            edition.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))
            //ISBN del Libro
            val isbn = TextView(this)
            val isbntxt = book?.isbn
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
                navigateToBook(book.isbn)
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

    override fun navigateToBook(isbn: String) {
        NeLSProject.book = isbn
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

