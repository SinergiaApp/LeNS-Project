package com.sinergia.eLibrary.presentation.Account.Presenter

import android.net.Uri
import android.util.Log
import com.sinergia.eLibrary.base.Exceptions.*
import com.sinergia.eLibrary.data.Model.Loan
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.domain.interactors.AccountInteractor.AccountInteractor
import com.sinergia.eLibrary.presentation.Account.AccountContract
import com.sinergia.eLibrary.presentation.Account.Model.AccountViewModel
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class AccountPresenter(accountViewModel: AccountViewModel, accountInteractor: AccountInteractor): AccountContract.AccountPresenter, CoroutineScope {

    val TAG = "[ACCOUNT_ACTIVITY]"

    var view: AccountContract.AccountView ?= null
    private var accountViewModel: AccountViewModel ?= null
    private var accountInteractor: AccountInteractor ?= null

    private val accountJob = Job()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + accountJob

    init{
        this.accountViewModel = accountViewModel
        this.accountInteractor = accountInteractor
    }


    override fun attachView(view: AccountContract.AccountView) {
        this.view = view
    }

    override fun dettachView() {
        this.view = null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun isViewAttach(): Boolean {
        return this.view !== null
    }

    override fun userCanBeDeleted(reserves: MutableList<String>, loans: MutableList<String>): Boolean {
        return (checkEmptyAcountReserves(reserves) && checkEmptyAcountLoans(loans))
    }

    override fun checkEmptyAcountReserves(reserves: MutableList<String>): Boolean {
        return reserves.isEmpty()
    }

    override fun checkEmptyAcountLoans(loans: MutableList<String>): Boolean {
        return loans.isEmpty()
    }


    override fun logOut() {
        launch{
            accountInteractor?.logOut()
            if(isViewAttach()) view?.navigateToMainPage()
        }
    }

    override fun updateAccount(newUserAccount: User) {

        if(isViewAttach()){
            view?.disableAllButtons()
            view?.showProgressBar()
        }

        launch{

            Log.d(TAG, "Trying to update account with email ${newUserAccount.email}.")

            NeLSProject.currentUser = newUserAccount

            try{

                accountInteractor?.updateAccount(newUserAccount)
                accountViewModel?.deleteUserForUpdate(newUserAccount)
                accountViewModel?.addUserForUpdate(newUserAccount)
                var userReserves = accountViewModel?.getUserPendingReserves(NeLSProject.currentUser.email)
                var userLoans = accountViewModel?.getUserPendingLoans(NeLSProject.currentUser.email)

                if(isViewAttach()){
                    view?.showMessage("Tu cuenta ha sido actualizada correctamente.")
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                    view?.initAccountContent(userReserves!!, userLoans!!)
                }

                Log.d(TAG, "Succesfully update account with email ${newUserAccount.email}.")

            } catch (error: FirebaseSetUserException){

                val errorMsg = error.message
                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                    getUserReservesAndLoans()
                }

                Log.d(TAG, "ERROR: Cannot update account with email ${newUserAccount.email} --> $errorMsg.")

            } catch(error: FirebaseAddUserException){

                val errorMsg = error.message
                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                    getUserReservesAndLoans()
                }


                Log.d(TAG, "ERROR: Cannot update account with email ${newUserAccount.email} --> $errorMsg.")

            }

        }


    }

    override fun deleteAccount(user: User) {

        if(isViewAttach()){
            view?.disableAllButtons()
            view?.showProgressBar()
        }

        launch{

            Log.d(TAG, "Trying to delete account with email ${user.email}.")

            try{

                accountInteractor?.deleteUser(user)
                accountViewModel?.deleteAccount(user)

                if(isViewAttach()){
                    view?.showMessage("Tu cuenta ha sido eliminada permanentemente.")
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                    view?.navigateToMainPage()
                }

                Log.d(TAG, "Succesfully delete account with email ${user.email}.")

            } catch(error: FirebaseDeleteUserException){

                val errorMsg = error.message
                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                }

                Log.d(TAG, "ERROR: Cannot delete account with email ${user.email} --> $errorMsg.")

            }

        }
    }

    override fun uploadImage(imageURI: Uri) {

        if(isViewAttach()){
            view?.disableAllButtons()
            view?.showProgressBar()
        }

        launch{

            Log.d(TAG, "Trying to update avatar image to account with email ${NeLSProject.currentUser.email}.")

            try{

                val newUserAvatar = accountViewModel?.uploadImage(NeLSProject.currentUser.email, imageURI)!!
                NeLSProject.currentUser.avatar = newUserAvatar.toString()
                accountViewModel?.setUserForUpdate(NeLSProject.currentUser)
                var userReserves = accountViewModel?.getUserPendingReserves(NeLSProject.currentUser.email)
                var userLoans = accountViewModel?.getUserPendingLoans(NeLSProject.currentUser.email)

                if(isViewAttach()){
                    view?.showMessage("Tu avatar ha sido actualizado correctamente.")
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                    view?.initAccountContent(userReserves!!, userLoans!!)
                }

            } catch (error: FirebaseStorageUploadImageException){

                val errorMsg = error.message
                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                }

                Log.d(TAG, "Cannot update avatar image to account with email ${NeLSProject.currentUser.email} --> $errorMsg.")

            } catch (error: FirebaseSetUserException){

                val errorMsg = error.message
                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                }

                Log.d(TAG, "Cannot update avatar image to account with email ${NeLSProject.currentUser.email} --> $errorMsg.")

            }


        }
    }

    override fun getUserReservesAndLoans() {

        Log.d(TAG, "Trying to get peding reserves and loans to user with email ${NeLSProject.currentUser.email}.")

        launch{

            try{

                if(isViewAttach()){
                    view?.disableAllButtons()
                    view?.showProgressBar()
                }

                var userReserves = accountViewModel?.getUserPendingReserves(NeLSProject.currentUser.email)
                var userLoans = accountViewModel?.getUserPendingLoans(NeLSProject.currentUser.email)

                if(isViewAttach()){
                    view?.enableAllButtons()
                    view?.hideProgressBar()
                    view?.initAccountContent(userReserves!!, userLoans!!)
                }

            } catch (error: FirebaseGetUserReservesException) {

                val errorMsg = error.message
                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                }

                Log.d(TAG, "Cannot get peding reserves to user with email ${NeLSProject.currentUser.email} --> $errorMsg.")

            } catch (error: FirebaseGetUserLoansException) {

                val errorMsg = error.message
                if(isViewAttach()){
                    view?.showError(errorMsg)
                    view?.hideProgressBar()
                    view?.enableAllButtons()
                }

                Log.d(TAG, "Cannot get peding loans to user with email ${NeLSProject.currentUser.email} --> $errorMsg.")

            }
        }

    }

}