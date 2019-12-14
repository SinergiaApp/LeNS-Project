package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel

class NelsDataBase {

    val nelsDB= FirebaseFirestore.getInstance()

    //USERS METHODS
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


    //RESOURCES METHODS
    fun getAllResourcesToCatalog(listener: CatalogViewModel.CatalogViewModelCallBack){

        var resourcesList: ArrayList<Resource> = arrayListOf<Resource>()

        nelsDB.collection("resources").get().addOnCompleteListener{ resources ->
            if(resources.isSuccessful){

                for (resource in resources.getResult()!!){
                    val inputResource: Resource = resource.toObject(Resource::class.java)
                    resourcesList.add(inputResource)
                }
                listener.onGetResourcesSuccess(resourcesList)

            } else {
                listener.onGetResourcesFailure(resources.exception.toString())
            }
        }

    }

    fun addResource(titulo: String, autor: String, iban: String, edicion: String, sinopsis: String, listener: AdminViewModel.createResourceCallBack){

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


    //LIBRARY METHODS
    fun addLibrary(nombre: String, direccion: String, geopoint: GeoPoint, listener: AdminViewModel.createLibrarylCallBack){

        val newLibrary: HashMap<String, Any> = hashMapOf(
            "name" to nombre,
            "direccion" to direccion,
            "geopoint" to geopoint
        )

        nelsDB
            .collection("libraries")
            .add(newLibrary)
            .addOnCompleteListener {newLibrary ->
                if(newLibrary.isSuccessful){
                    listener.onCreateLibrarySuccess()
                } else {
                    listener.onCreateLibraryFailure(newLibrary.exception.toString())
                }
            }

    }

}