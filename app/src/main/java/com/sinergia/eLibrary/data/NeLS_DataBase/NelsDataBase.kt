package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.firestore.FirebaseFirestore
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel

class NelsDataBase {

    val nelsDB= FirebaseFirestore.getInstance()


    fun getAllResourcesToCatalog(callBack: CatalogViewModel.CatalogViewModelCallBack){

        nelsDB.collection("resources").get().addOnCompleteListener{resources ->
            if(resources.isSuccessful){
                //devuelve ls recursos para cargarlos en la vista
            } else {
                //devuelve un error en formato String
            }
        }

    }

    fun addUser(name: String, lastName: String, email: String, password: String, admin: Boolean){

        val newUser: HashMap<String, Any> = hashMapOf(
            "name" to name,
            "lastName" to lastName,
            "email" to email,
            "password" to password,
            "admin" to admin
        )

        nelsDB
            .collection("users")
            .add(newUser)
            .addOnCompleteListener{ adduser ->
                if(adduser.isSuccessful){
                    //TODO: Mensaje de Usuario agregado a la BBDD correctamente.
                    //TODO: Este método va sujeto aque la creación del usuario en FireBase de Google tenga éxtito.
                } else {
                    //TODO: Mensaje de error al crear el usuario.
                }
            }

    }

    fun setUser(field: String, newValue: String){

    }

    fun addResource(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String, listener: AdminViewModel.AdminViewModelCallBack){

        val newResource = hashMapOf<String, Any>(

            "title" to titulo,
            "author" to autor,
            "iban" to iban,
            "edition" to edicion,
            "sinopsis" to sinopsis

        )

        nelsDB
            .document("resources/$iban")
            .set(newResource)
            .addOnCompleteListener{addresource ->

                if(addresource.isSuccessful){
                    listener.onCreateResourceSuccess()
                } else {
                    listener.onCreateResourceFailure(addresource.exception?.message.toString())
                }

            }

    }

    fun setResource(){

    }



}