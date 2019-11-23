package com.sinergia.eLibrary.presentation.MainPage.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.MainPage.MainPageContract
import kotlinx.android.synthetic.main.activity_main_page.*

class MainPage : BaseActivity(), MainPageContract {


    //BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        header_logout_btn.setOnClickListener{ logout() }
        header_user_name.text = FirebaseAuth.getInstance().currentUser?.displayName.toString()

    }

    override fun getLayout(): Int {
        return R.layout.activity_main_page
    }

    override fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, MainActivity::class.java))
    }


}

