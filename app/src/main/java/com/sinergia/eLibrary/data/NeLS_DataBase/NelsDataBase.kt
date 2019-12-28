package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.base.Exceptions.FirebaseCreateLibraryException
import com.sinergia.eLibrary.base.Exceptions.FirebaseCreateResourceException
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetAllResourcesException
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetAllLibrariesException
import com.sinergia.eLibrary.base.Exceptions.FirebaseGetUserException
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

    suspend fun getCurrentUser(){

    }

    suspend fun getUser(email: String): User = suspendCancellableCoroutine{getUserContinue ->

        nelsDB
            .collection("users")
            .document(email)
            .get()
            .addOnCompleteListener {user ->
                if(user.isSuccessful){
                    val currentUser = user.getResult()!!.toObject(User::class.java)
                    getUserContinue.resume(currentUser!!)
                } else {
                    getUserContinue.resumeWithException(
                        FirebaseGetUserException(
                            user.exception?.message.toString()
                        )
                    )
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
                    getAllResourcesContinue.resumeWithException(
                        FirebaseGetAllResourcesException(
                            resources.exception?.message.toString()
                        )
                    )
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
                    addResourceContinuation.resumeWithException(
                        FirebaseCreateResourceException(
                            addresource.exception?.message.toString()
                        )
                    )
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
                    addLibraryContinuation.resumeWithException(
                        FirebaseCreateLibraryException(
                            newLibrary.exception?.message.toString()
                        )
                    )
                }
            }

    }

    suspend fun getAllLibraries(): ArrayList<Library> = suspendCancellableCoroutine {getAllLibrariesContinue ->

        var librariesList = arrayListOf<Library>()

        nelsDB
            .collection("libraries")
            .get()
            .addOnCompleteListener {libraries ->

                if(libraries.isSuccessful){

                    for (library in libraries.getResult()!!){

                        val inputLibrary = library.toObject(Library::class.java)
                        librariesList.add(inputLibrary)

                    }

                    getAllLibrariesContinue.resume(librariesList)

                } else {

                    getAllLibrariesContinue.resumeWithException(
                        FirebaseGetAllLibrariesException(
                            libraries.exception?.message.toString()
                        )
                    )

                }

            }

    }

}