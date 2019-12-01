package com.sinergia.eLibrary.presentation

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import kotlinx.android.synthetic.main.activity_drawer_menu.*

class DrawerMenu : BaseActivity() {
    override fun getLayout(): Int {
        return R.layout.activity_drawer_menu
    }

    override fun getPageTitle(): String {
        return "MENU DESPLEGABLE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drawer_menu)

        initDrawer(drawer_content)

    }

    private fun getTitles(): ArrayList<HashMap<String, Any>>{
        return arrayListOf<HashMap<String, Any>>(

            hashMapOf(
                "titulo" to "Catálogo",
                "logo" to com.sinergia.eLibrary.R.drawable.logonels
            ),
            hashMapOf(
                "titulo" to "Catálogo",
                "logo" to com.sinergia.eLibrary.R.drawable.logonels
            ),hashMapOf(
                "titulo" to "Catálogo",
                "logo" to com.sinergia.eLibrary.R.drawable.logonels
            ),hashMapOf(
                "titulo" to "Catálogo",
                "logo" to com.sinergia.eLibrary.R.drawable.logonels
            ),hashMapOf(
                "titulo" to "Catálogo",
                "logo" to com.sinergia.eLibrary.R.drawable.logonels
            )

        )
    }

    private fun createLogo(logoImage: Int): RelativeLayout {
        val logoContainer = RelativeLayout(this)
        val logoContainerLayoutParams = LinearLayout.LayoutParams(24, 24)
        logoContainer.setLayoutParams(logoContainerLayoutParams)

        val logo = ImageView(this)
        logo.setImageResource(logoImage)
        logoContainer.addView(logo)

        return logoContainer

    }

    private fun createTitle(titleString: String): TextView {

        val title = TextView(this)
        title.setText(titleString)
        title.setTextColor(Color.parseColor("#B4ACB8"))
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24f)
        title.setPadding(10, 0, 0, 0)

        return title

    }

    private fun initDrawer(content: LinearLayoutCompat){

        drawer_header_userimage.setImageResource(R.drawable.logonels)
        drawer_header_username.setText("Nombre de Usuario")
        drawer_header_usermail.setText("Email de Usuario")

        val titulos = getTitles()

        for(linea in titulos){

            var logoLinea = createLogo(linea.get("logo") as Int)
            var textoLinea = createTitle(linea.get("titulo").toString())
            var lineaContainer = LinearLayout(this)
            lineaContainer

            lineaContainer.addView(logoLinea)
            lineaContainer.addView(textoLinea)

            content.addView(lineaContainer)

        }


    }


}
