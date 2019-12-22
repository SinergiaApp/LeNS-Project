package com.sinergia.eLibrary.presentation.Libraries.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.presentation.Libraries.LibrariesContract
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import kotlinx.android.synthetic.main.activity_catalog.*
import kotlinx.android.synthetic.main.activity_libraies.*
import kotlinx.android.synthetic.main.activity_libraies.menu_button

class LibraiesActivity : BaseActivity(), LibrariesContract.LibrariesView {

    //ACTIVITY TITLE
    override fun getPageTitle(): String {
        return "BIBLIOTECAS"
    }

    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }



    }


    override fun getLayout(): Int {
        return R.layout.activity_libraies
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

    override fun initLibrariesContent(liraries: List<Library>) {
        //TODO: Función que inicia el contenido del layout a partir de un List de Bibliotecas que le llega de la base de datos.
    }


}
