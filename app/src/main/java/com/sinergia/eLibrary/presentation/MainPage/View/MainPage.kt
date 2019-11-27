package com.sinergia.eLibrary.presentation.MainPage.View

import android.content.Intent
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.MainPage.MainPageContract
import kotlinx.android.synthetic.main.activity_main_page.*
import android.widget.*


class MainPage : BaseActivity(), MainPageContract {


    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header_logout_btn.setOnClickListener{ logout() }
        header_user_name.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()

        initContent(main_page_content)

    }

    override fun getLayout(): Int {
        return R.layout.activity_main_page
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
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

}

