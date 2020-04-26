package com.sinergia.eLibrary.presentation.Libraries.View

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.presentation.Libraries.LibrariesContract
import com.sinergia.eLibrary.presentation.Libraries.Model.LibrariesViewModel
import com.sinergia.eLibrary.presentation.Libraries.Model.LibrariesViewModelImpl
import com.sinergia.eLibrary.presentation.Libraries.Presenter.LibrariesPresenter
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.android.synthetic.main.activity_libraies.*
import kotlinx.android.synthetic.main.layout_headder_bar.*

class LibraiesActivity : BaseActivity(), LibrariesContract.LibrariesView {

    private lateinit var librariesPresenter: LibrariesContract.LibrariesPresenter
    private lateinit var librariesViewModel: LibrariesViewModel

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        librariesPresenter = LibrariesPresenter(LibrariesViewModelImpl())
        librariesPresenter.attachView(this)
        librariesViewModel = ViewModelProviders.of(this).get(LibrariesViewModelImpl::class.java)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        librariesPresenter.getAllLibraries()

    }


    override fun getLayout(): Int {
        return R.layout.activity_libraies
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_LIBRARIES)
    }

    //LIBRARIES CONTRACT METHODS
    override fun showError(errorMsg: String?) {
        toastL(this, errorMsg)
    }

    override fun showMessage(message: String?) {
        toastS(this, message)
    }

    override fun showLibrariesProgressBar() {
        libraries_progressBar.visibility = View.VISIBLE
    }

    override fun hideLibrariesProgressBar() {
        libraries_progressBar.visibility = View.GONE
    }

    override fun initLibrariesContent(librariesList: ArrayList<Library>?) {

        if(librariesList?.isEmpty()!!){
            toastL(this, "Vaya... Parece que no hay ningún recurso en la Base de Datos...")
        } else {

            for (library in librariesList) {
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
                resource.orientation = LinearLayout.VERTICAL
                resource.setBackground(ContextCompat.getDrawable(this, R.drawable.style_list_resource))
                resource.layoutParams = params
                resource.setPadding(20,10,20,10)
                val description = LinearLayout(this)
                description.setOrientation(LinearLayout.VERTICAL)


                val imageLayout = RelativeLayout(this)
                val image = ImageView(this)
                image.setImageResource(R.drawable.icon_logonels)
                imageLayout.addView(image)
                val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250)
                imageLayout.setLayoutParams(layoutParams)

                //Nombre de la Biblioteca
                val name = TextView(this)
                val nametxt = library.name
                name.setText(nametxt)
                name.setTextSize(TypedValue.COMPLEX_UNIT_SP,30.toFloat())
                name.setTextColor(ContextCompat.getColor(this, R.color.colorBlack))

                resource.addView(name)
                resource.addView(imageLayout)
                resource.setOnClickListener {
                    navigateToLibrary(library)
                }
                libraries_content.addView(resource)
            }

        }
    }

    override fun navigateToLibrary(library: Library) {
        NeLSProject.currentLibrary = library
        startActivity(Intent(this, LibraryActivity::class.java))
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        librariesPresenter.dettachView()
    }

    override fun onDestroy() {
        super.onDestroy()
        librariesPresenter.dettachView()
    }

}
