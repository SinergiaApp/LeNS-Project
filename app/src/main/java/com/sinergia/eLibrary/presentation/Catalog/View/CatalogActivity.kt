package com.sinergia.eLibrary.presentation.Catalog.View

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Catalog.CatalogContract
import kotlinx.android.synthetic.main.activity_catalog.*
import android.widget.*
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity


class CatalogActivity: BaseActivity(), CatalogContract.CatalogView {

    //ACTIVITY TITLE
    override fun getPageTitle(): String {
        return "CATÁLOGO"
    }


    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        main_page_title.text = getPageTitle()

        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        initContent(main_page_content)

    }

    override fun getLayout(): Int {
        return R.layout.activity_catalog
    }

    //CATALOG CONTRACT METHODS
    override fun showError(error: String) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastS(this, message)
    }

    override fun initCatalog(resourcesList: ArrayList<Resource>) {
        //TODO: Función que inicia el catálogo con el contenido que le devuelve la consulta a la Base de Datos.
    }

    fun initContent(content: LinearLayout){
        for(i in 1..20){
            val resource = LinearLayout(this)
            resource.setOrientation(LinearLayout.HORIZONTAL)
            val description = LinearLayout(this)
            description.setOrientation(LinearLayout.VERTICAL)


            val imageLayout = RelativeLayout(this)
            val image = ImageView(this)
            image.setImageResource(R.drawable.logonels)
            imageLayout.addView(image)
            val layoutParams = LinearLayout.LayoutParams(250, 250)
            imageLayout.setLayoutParams(layoutParams)

            val title = TextView(this)
            title.setText("Título: <Título del Libro $i.>")
            description.addView(title)
            val edition = TextView(this)
            edition.setText("Edición: <Edición del Libro $i>")
            description.addView(edition)
            val iban = TextView(this)
            iban.setText("IBAN: <IBAN del Libro $i>")
            description.addView(iban)

            resource.addView(imageLayout)
            resource.addView(description)

            content.addView(resource)
        }
    }

    fun showHideMenu(drawer: View){
        if(drawer.visibility == View.VISIBLE){
            drawer.visibility = View.GONE
        } else {
            drawer.visibility = View.VISIBLE
        }
    }

    override fun goToMainMenu() {
        val mainMenuIntent = Intent(this, MainMenuActivity::class.java)
        val activityName : String = getPageTitle()
        mainMenuIntent.putExtra("activityName", activityName)
        startActivity(mainMenuIntent)
    }

}

