package com.sinergia.eLibrary.presentation.Account.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.get
import com.sinergia.eLibrary.R
import com.sinergia.eLibrary.base.BaseActivity
import com.sinergia.eLibrary.domain.interactors.AccountInteractor.AccountInteractor
import com.sinergia.eLibrary.domain.interactors.AccountInteractor.AccountInteractorImpl
import com.sinergia.eLibrary.presentation.Account.AccountContract
import com.sinergia.eLibrary.presentation.Account.Model.AccountViewModel
import com.sinergia.eLibrary.presentation.Account.Model.AccountViewModelImpl
import com.sinergia.eLibrary.presentation.Account.Presenter.AccountPresenter
import com.sinergia.eLibrary.presentation.Main.View.MainActivity
import kotlinx.android.synthetic.main.activity_account.*

class Account : BaseActivity(), AccountContract.AccountView {

    var TAG = "[ACCOUNT_ACTIVITY]"

    lateinit var accountPresenter: AccountPresenter
    lateinit var accountViewModel: AccountViewModel

    // BASE ACTIVITY METHODS
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        accountPresenter = AccountPresenter(AccountViewModelImpl(), AccountInteractorImpl())
        accountPresenter.attachView(this)
        accountViewModel = ViewModelProviders.of(this).get(AccountViewModelImpl::class.java)

    }

    override fun getLayout(): Int {
        return R.layout.activity_account
    }

    override fun getPageTitle(): String {
        return "Mi Cuenta"
    }

    // ACOUNT VIEW METHODS
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
    }

    override fun disableAllButtons() {
        account_update_btn.isEnabled = false
        account_update_btn.isClickable = false
        account_delete_btn.isEnabled = false
        account_delete_btn.isClickable = false
    }

    override fun logOut() {
        accountPresenter.logOut()
    }

    override fun updateAccount() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAccount() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun refresh() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun navigateToMainPage() {
        val intentMainPage = Intent(this, MainActivity::class.java)
    }

}
