package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.presentation.AdminZone.Exceptions.FirebaseCreateLibraryException
import com.sinergia.eLibrary.presentation.AdminZone.Exceptions.FirebaseCreateResourceException
import com.sinergia.eLibrary.presentation.AdminZone.Model.AdminViewModel
import com.sinergia.eLibrary.presentation.Catalog.Exceptions.FirebaseGetAllResourcesException
import com.sinergia.eLibrary.presentation.Catalog.Model.CatalogViewModel
import com.sinergia.eLibrary.presentation.Register.FirebaseAddUserException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NelsDataBase {

    val nelsDB= FirebaseFirestore.getInstance()

    //USERS METHODS
    suspend fun addUser(name: String, lastName: String, email: String, admin: Boolean, resources: Map<String, String>): Unit = suspendCancellableCoroutine{addUserContinuation ->

        val newUser: HashMap<String, Any> = hashMapOf(
            "name" to name,
            "lastName" to lastName,
            "email" to email,
            "admin" to admin,
            "resources" to resources
        )

        nelsDB
            .document("users/$email")
            .set(newUser)
            .addOnCompleteListener{ adduser ->
                if(adduser.isSuccessful){
                    addUserContinuation.resume(Unit)
                } else {
                    addUserContinuation.resumeWithException(FirebaseAddUserException(adduser.exception?.message.toString()))
                }
            }

    }

    fun setUser(field: String, newValue: String){

    }


    //RESOURCES METHODS
    suspend fun getAllResources(): ArrayList<Resource> = suspendCancellableCoroutine{getAllResourcesContinue ->

        var resourcesList: ArrayList<Resource> = arrayListOf()

        nelsDB
            .collection("resources")
            .get()
            .addOnCompleteListener{ resources ->
                if(resources.isSuccessful){

                    for (resource in resources.getResult()!!){
                        val inputResource: Resource = resource.toObject(Resource::class.java)
                        resourcesList.add(inputResource)
                    }
                    getAllResourcesContinue.resume(resourcesList)

                } else {
                    getAllResourcesContinue.resumeWithException(FirebaseGetAllResourcesException(resources.exception?.message.toString()))
                }
            }

    }

    fun getResource(isbn: String){

        nelsDB
            .collection("resources/$isbn")
            .get()
            .addOnCompleteListener {resource ->

                if(resource.isSuccessful){
                    //TODO: CallBack de onGetResourceSuccess. Pendiente de programar.
                    //TODO: Recuerda, hay que devolver en el CallBack resource.toObject(Resource::class.java)
                } else {
                  //TODO: CallBack de onGetResourcFailure. Pendiente de programar.
                }

            }

    }

    suspend fun addResource(titulo: String, autor: String, isbn: String, edicion: String, editorial: String, sinopsis: String): Unit = suspendCancellableCoroutine{addResourceContinuation ->

        val newResource = hashMapOf<String, Any>(

            "title" to titulo,
            "author" to autor,
            "isbn" to isbn,
            "edition" to edicion,
            "publisher" to editorial,
            "sinopsis" to sinopsis

        )

        nelsDB
            .document("resources/$isbn")
            .set(newResource)
            .addOnCompleteListener{addresource ->

                if(addresource.isSuccessful){
                    addResourceContinuation.resume(Unit)
                } else {
                    addResourceContinuation.resumeWithException(FirebaseCreateResourceException(addresource.exception?.message.toString()))
                }

            }

    }

    fun setResource(){

    }


    //LIBRARY METHODS
    suspend fun addLibrary(nombre: String, direccion: String, geopoint: GeoPoint): Unit = suspendCancellableCoroutine{addLibraryContinuation ->

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
                    addLibraryContinuation.resume(Unit)
                } else {
                    addLibraryContinuation.resumeWithException(FirebaseCreateLibraryException(newLibrary.exception?.message.toString()))
                }
            }

    }

}