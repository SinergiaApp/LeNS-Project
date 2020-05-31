package com.sinergia.eLibrary.presentation.Account.View

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.data.Model.Loan
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.interactors.AccountInteractor.AccountInteractorImpl
import com.sinergia.eLibrary.presentation.Account.AccountContract
import com.sinergia.eLibrary.presentation.Account.Model.AccountViewModel
import com.sinergia.eLibrary.presentation.Account.Model.AccountViewModelImpl
import com.sinergia.eLibrary.presentation.Account.Presenter.AccountPresenter
import com.sinergia.eLibrary.presentation.Dialogs.ConfirmDialog.ConfirmDialogActivity
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import com.sinergia.eLibrary.presentation.MainMenu.View.MainMenuActivity
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.android.synthetic.main.activity_account.*
import kotlinx.android.synthetic.main.layout_headder_bar.*
import net.glxn.qrgen.android.QRCode

class AccountActivity : BaseActivity(), AccountContract.AccountView {

    var TAG = "[ACCOUNT_ACTIVITY]"

    lateinit var accountPresenter: AccountPresenter
    lateinit var accountViewModel: AccountViewModel

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        page_title.text = getPageTitle()
        menu_button.setOnClickListener { startActivity(Intent(this, MainMenuActivity::class.java)) }

        accountPresenter = AccountPresenter(AccountViewModelImpl(), AccountInteractorImpl())
        accountPresenter.attachView(this)
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModelImpl::class.java)

        account_userAvatar.setOnClickListener { uploadGalleryImage() }
        account_logout.setOnClickListener { logOut() }
        account_update_btn.setOnClickListener { updateAccount() }
        account_delete_btn.setOnClickListener { deleteAccount() }

        getUserReservesAndLoans()

    }

    override fun getLayout(): Int {
        return R.layout.activity_account
    }

    override fun getPageTitle(): String {
        return getString(R.string.PG_ACCOUNT)
    }

    // ACCOUNT VIEW METHODS
    override fun showError(error: String?) {
        toastL(this, error)
    }

    override fun showMessage(message: String) {
        toastL(this, message)
    }

    override fun showProgressBar() {
        accountProgressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        accountProgressBar.visibility = View.INVISIBLE
    }

    override fun enableAllButtons() {
        account_update_btn.isEnabled = true
        account_update_btn.isClickable = true
        account_delete_btn.isEnabled = true
        account_delete_btn.isClickable = true
        account_userAvatar.isClickable = true
    }

    override fun disableAllButtons() {
        account_update_btn.isEnabled = false
        account_update_btn.isClickable = false
        account_delete_btn.isEnabled = false
        account_delete_btn.isClickable = false
        account_userAvatar.isClickable = false
    }

    override fun initAccountContent(userReserves: ArrayList<Reserve>, userLoans: ArrayList<Loan>) {

        val currentUser = NeLSProject.currentUser

        if(currentUser.avatar != "SinAvatar"){
            Glide
                .with(this)
                .load(Uri.parse(currentUser.avatar))
                .fitCenter()
                .centerCrop()
                .into(account_userAvatar)
        }

        account_nameHead.text = "${currentUser.name} ${NeLSProject.currentUser.lastName1}"
        account_mailHead.text = currentUser.email

        account_userName.setText(currentUser.name)
        account_userLastName1.setText(currentUser.lastName1)
        account_userLastName2.setText(currentUser.lastName2)
        account_userMail.setText(currentUser.email)
        account_userNIF.setText(currentUser.nif)

        if(currentUser.reserves.size > 0){
            account_reserves.removeAllViews()
            for(reserve in userReserves){
                var currentReserve = TextView(this)
                currentReserve.text = "(${reserve.reserveDate}) - ${reserve.resourceName}"
                account_reserves.addView(currentReserve)
            }
        }

        if(currentUser.loans.size > 0){
            account_loans.removeAllViews()
            for(loan in userLoans){
                var currentLoan = TextView(this)
                currentLoan.text = "(${loan.loanDate}) - ${loan.resourceName}"
                account_loans.addView(currentLoan)
            }
        }

        account_userQR.setImageBitmap(QRCode.from(currentUser.email).bitmap())

    }

    override fun logOut() {
        accountPresenter.logOut()
    }

    override fun updateAccount() {

        var newName = account_userName.text.toString()
        var newLastName1 = account_userLastName1.text.toString()
        var newLastName2 = account_userLastName2.text.toString()
        var newEmail = account_userMail.text.toString()
        var newNIF = account_userNIF.text.toString()

        var newUserAccount = User(
            newName,
            newLastName1,
            newLastName2,
            newEmail,
            newNIF,
            NeLSProject.currentUser.reserves,
            NeLSProject.currentUser.loans,
            NeLSProject.currentUser.favorites,
            NeLSProject.currentUser.admin,
            NeLSProject.currentUser.avatar
        )

        accountPresenter.updateAccount(newUserAccount)

    }

    override fun deleteAccount() {

        val reserveDialog = ConfirmDialogActivity
            .Buider()
            .setTitleText("Confirmar Eliminación Permanente de Cuenta")
            .setDescriptionText(
                "Está a punto de elimnar de forma permanente su cuenta de la aplicación NeLS  " +
                        "(Neurolingüistic eLibrary for Students), ¿Desea continuar?."
            )
            .setAcceptButtonText("CONTINUAR")
            .setCancelButtonText("CANCELAR")
            .buid()

        reserveDialog.show(supportFragmentManager!!, "ReserveDialog")
        reserveDialog.isCancelable = false
        reserveDialog.setDialogOnClickButtonListener(object: ConfirmDialogActivity.DialogOnClickButtonListener{
            override fun clickAcceptButton() {
                if(!accountPresenter.userCanBeDeleted(NeLSProject.currentUser.reserves, NeLSProject.currentUser.loans)){

                    if(!accountPresenter.checkEmptyAcountReserves(NeLSProject.currentUser.reserves)) showError("Tienes reservas pendientes, no puedes eliminar tu cuenta.")
                    if(!accountPresenter.checkEmptyAcountLoans(NeLSProject.currentUser.loans)) showError("Tienes reservas pendientes, no puedes eliminar tu cuenta.")

                } else {
                    accountPresenter.deleteAccount(NeLSProject.currentUser)
                }
                reserveDialog.dismiss()
            }

            override fun clickCancelButton() {
                reserveDialog.dismiss()
            }

        })

    }

    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, MainActivity::class.java)
        intentMainPage.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intentMainPage)
    }

    // GALLERY METHODS
    override fun uploadGalleryImage() {

        if(NeLSProject.storagePermissionGranted){
            val galleryIntent = Intent(Intent.ACTION_PICK)
            galleryIntent.type = "image/*"
            startActivityForResult(galleryIntent, NeLSProject.GALLERY_INTENT_CODE)
        } else {
            toastL(this, "Por favor permite que la app acceda al almacenamiento del dispositivo.")
            checkAndSetGalleryPermissions()
        }

    }

    override fun getUserReservesAndLoans() {
        accountPresenter.getUserReservesAndLoans()
    }

    // ACTICITY RESULTS METHODS
    override fun checkAndSetGalleryPermissions() {
        val permissionStatusRead = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if(permissionStatusRead == PackageManager.PERMISSION_GRANTED) {
            NeLSProject.storagePermissionGranted = true
        } else {
            if (permissionStatusRead != PackageManager.PERMISSION_GRANTED) ActivityCompat.requestPermissions(this, arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE), NeLSProject.READ_STORAGE_PERMISSIONS_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, imageData: Intent?) {

        super.onActivityResult(requestCode, resultCode, imageData)

        if(requestCode == NeLSProject.GALLERY_INTENT_CODE && resultCode == Activity.RESULT_OK){

            var imageURI: Uri = imageData?.data!!
            accountPresenter?.uploadImage(imageURI)

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when(requestCode) {
            NeLSProject.READ_STORAGE_PERMISSIONS_CODE ->
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    NeLSProject.storagePermissionGranted = (
                            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                } else {
                    toastL(this, "No se podrá acceder al almacenamiento del dispositivo hasta que concedas todos los permisos.")
                }
        }
    }

}
