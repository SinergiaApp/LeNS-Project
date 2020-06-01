package com.sinergia.eLibrary.presentation.Catalog.Presenter

import android.util.Log
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetResourceException
import com.sinergia.eLibrary.base.Exceptions.FirebaseSetResourceException
import com.sinergia.eLibrary.data.Model.Reserve
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract
import com.sinergia.eLibrary.presentation.Catalog.ItemCatalogContract.ItemCatalogView
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.ItemCatalogViewModelImpl
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.coroutines.*
import java.time.LocalDateTime
import kotlin.coroutines.CoroutineContext

class ItemCatalogPresenter(itemCatalogViewModel: ItemCatalogViewModelImpl): ItemCatalogContract.ItemCatalogPresenter, CoroutineScope {

    val TAG = "[ITEM_CAT_ACTIVITY]"

    var view: ItemCatalogView ?= null
    var itemCatalogViewModel: ItemCatalogViewModel ?= null
    val itemCatalogJob = Job()

    init{
        this.itemCatalogViewModel = itemCatalogViewModel
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + itemCatalogJob



    override fun attachView(view: ItemCatalogView) {
        this.view = view
    }

    override fun dettachView() {
        view = null
    }

    override fun isViewAttached(): Boolean {
        return view != null
    }

    override fun dettachJob() {
        coroutineContext.cancel()
    }

    override fun chekRepeatLikeDislike(list: MutableList<String>): Boolean {
        return NeLSProject.currentUser.email in list
    }

    override fun checkUserCanDoReserve(): Boolean {

        var res = true

        if(NeLSProject.currentUser.reserves.contains(NeLSProject.currentResource!!.isbn)){
            res = false
            view?.showError("Ya tienes reservado este recurso, no puedes reservarlo dos veces.")
        } else if(NeLSProject.currentUser.reserves.size == 3){
            res = false
            view?.showError("Tienes 3 reservas actualmente, no puedes hacer mas hasta que finalices alguna.")
        }

        return res

    }

    override fun getItemCatalog(isbn: String) {

        launch {

            view?.showItemCatalogProgressBar()

            try{

                NeLSProject.currentResource = itemCatalogViewModel?.getItemCatalog(isbn)
                val libraries = itemCatalogViewModel?.getAllLibraries()

                if(isViewAttached()){
                    view?.hideItemCatalogProgressBar()
                    view?.showItemCatalogContent()
                    view?.initItemCatalogContent(NeLSProject.currentResource, libraries)
                }

                Log.d(TAG, "Succesfullt get ItemCatalog Resource.")

            }catch (error: FirebaseGetResourceException){

                var errorMsg = error.message

                if(isViewAttached()) {
                    view?.showError(errorMsg)
                    view?.hideItemCatalogProgressBar()
                }

                Log.d(TAG, "ERROR: Cannot load IemCatalog Resource from DataBase --> $errorMsg")

            }

        }


    }

    override fun setResourceLikes(resource: Resource) {

        launch{

            view?.showItemCatalogProgressBar()

            try{
                itemCatalogViewModel?.setResource(resource)
                val libraries = itemCatalogViewModel?.getAllLibraries()
                if(isViewAttached()){
                    view?.hideItemCatalogProgressBar()
                    view?.showItemCatalogContent()
                    view?.initItemCatalogContent(resource, libraries)
                }
                view?.showMessage("¡Perfecto! Hemos guardado tu voto.")

            } catch (error: FirebaseSetResourceException){

                var errorMsg = error.message
                view?.showError(errorMsg)
                view?.hideItemCatalogProgressBar()


                Log.d(TAG, "ERROR: Cannot set IemCatalog Resource from DataBase --> $errorMsg")

            }

        }

    }



    override fun addUserReserve(userMail: String, resourceId: String, resourceName: String, libraryId: String) {

        var newResourceDisponibility = NeLSProject.currentResource!!.disponibility
        newResourceDisponibility.set(libraryId, newResourceDisponibility[libraryId]!!-1)
        var settedResource = Resource(
            NeLSProject.currentResource!!.title,
            NeLSProject.currentResource!!.author,
            NeLSProject.currentResource!!.publisher,
            NeLSProject.currentResource!!.edition,
            NeLSProject.currentResource!!.sinopsis,
            NeLSProject.currentResource!!.isbn,
            newResourceDisponibility,
            NeLSProject.currentResource!!.likes,
            NeLSProject.currentResource!!.dislikes,
            NeLSProject.currentResource!!.isOnline,
            NeLSProject.currentResource!!.urlOnline

        )

        val newReserve = Reserve(
            userMail,
            resourceId,
            resourceName,
            libraryId,
            LocalDateTime.now().toString()
        )


        val currentUser = NeLSProject.currentUser
        var newUserReserves = currentUser.reserves
        newUserReserves.add(resourceId)
        val settedUser = User(
            currentUser.name,
            currentUser.lastName1,
            currentUser.lastName2,
            currentUser.email,
            currentUser.nif,
            newUserReserves,
            currentUser.loans,
            currentUser.favorites,
            currentUser.admin,
            currentUser.researcher,
            currentUser.avatar
        )



        launch{

            view?.showItemCatalogProgressBar()

            try{
                itemCatalogViewModel?.setResource(settedResource)
                itemCatalogViewModel?.newReserve(newReserve)
                itemCatalogViewModel?.setUser(settedUser)
                if(isViewAttached()){
                    view?.hideItemCatalogProgressBar()
                    view?.showMessage("Tu Reserva se ha llevado a cabo con éxito, ¡Ya puedes ir a recogerlo!.")
                    view?.navigateToCatalog()
                }

                Log.d(TAG, "Succesfullt add Reserve to user ${NeLSProject.currentUser.email}.")

            } catch (error: FirebaseSetResourceException){

                var errorMsg = error.message
                view?.showError(errorMsg)
                view?.hideItemCatalogProgressBar()


                Log.d(TAG, "ERROR: Cannot add Reserve to user ${NeLSProject.currentUser.email} --> $errorMsg")

            }

        }

    }

    override fun cancelUserReserve(userMail: String, resourceId: String) {
        TODO("Not yet implemented")
    }

}