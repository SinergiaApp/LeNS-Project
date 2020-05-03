package com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.sinergia.eLibrary.R
import kotlinx.android.synthetic.main.activity_inform_dialog.*

class InformDialogActivity private constructor(

    private val acceptButtonText: String?,
    private val titleText: String?,
    private val descriptionText: String?,
    private var dialogOnClickButtonListener: DialogOnClickButtonListener ?= null

): DialogFragment() {

    interface DialogOnClickButtonListener{
        fun clickAcceptButton()
    }

    data class Buider(
        private var acceptButtonText: String? = null,
        private var titleText: String? = null,
        private var descriptionText: String? = null
    ){
        fun setAcceptButtonText(acceptButtonText: String) = apply { this.acceptButtonText = acceptButtonText }
        fun setTitleText(titleText: String) = apply { this.titleText = titleText }
        fun setDescriptionText(descriptionText: String) = apply { this.descriptionText = descriptionText }

        fun buid() = InformDialogActivity(acceptButtonText, titleText, descriptionText)
    }

    fun setDialogOnClickButtonListener(listener : DialogOnClickButtonListener) = apply { dialogOnClickButtonListener = listener }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_inform_dialog, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        inform_dialog_accept_btn.text = acceptButtonText
        inform_dialog_accept_btn.setOnClickListener { dialogOnClickButtonListener!!.clickAcceptButton() }
        inform_dialog_title.text = titleText
        inform_dialog_description.text = descriptionText
    }

}