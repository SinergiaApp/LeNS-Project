package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.base.Exceptions.*
import com.sinergia.eLibrary.data.Model.Library
import com.sinergia.eLibrary.data.Model.Resource
import com.sinergia.eLibrary.data.Model.User
import com.sinergia.eLibrary.base.Exceptions.FirebaseAddUserException
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NelsDataBase {

    val nelsDB= FirebaseFirestore.getInstance()

    //USERS METHODS
    suspend fun addUser(
        name: String,
        lastName1: String,
        lastName2: String,
        email: String,
        nif: String,
        loans: MutableList<String>,
        favorites: MutableList<String>,
        admin: Boolean
        ): Unit = suspendCancellableCoroutine{addUserContinuation ->

        val newUser: HashMap<String, Any> = hashMapOf(
            "name" to name,
            "lastName1" to lastName1,
            "lastName2" to lastName2,
            "email" to email,
            "nif" to nif,
            "loans" to loans,
            "favorites" to favorites,
            "admin" to admin
        )

        nelsDB
            .document("users/$email")
            .set(newUser)
            .addOnCompleteListener{ adduser ->
                if(adduser.isSuccessful){
                    addUserContinuation.resume(Unit)
                } else {
                    addUserContinuation.resumeWithException(
                        FirebaseAddUserException(
                            adduser.exception?.message.toString()
                        )
                    )
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

    suspend fun getResource(isbn: String): Resource = suspendCancellableCoroutine{ getResourceContinuation ->

        nelsDB
            .collection("resources")
            .document(isbn)
            .get()
            .addOnCompleteListener { resource ->

                if(resource.isSuccessful){

                    val resourceDB = resource.getResult()!!.toObject(Resource::class.java)
                    getResourceContinuation.resume(resourceDB!!)

                } else {

                    getResourceContinuation.resumeWithException(
                        FirebaseGetResourceException(
                            resource.exception?.message.toString()
                        )
                    )

                }

            }

    }

    suspend fun addResource(
        titulo: String,
        autores: List<String>,
        isbn: String,
        edicion: String,
        editorial: String,
        sinopsis: String,
        disponibility: MutableMap<String, Integer>,
        likes: MutableList<String>,
        dislikes: MutableList<String>,
        isOnline: Boolean,
        urlOnline: String): Unit = suspendCancellableCoroutine{addResourceContinuation ->

        val newResource = hashMapOf<String, Any>(

            "title" to titulo,
            "author" to autores,
            "isbn" to isbn,
            "edition" to edicion,
            "publisher" to editorial,
            "sinopsis" to sinopsis,
            "disponibility" to disponibility,
            "likes" to likes,
            "dislikes" to dislikes,
            "isOnline" to isOnline,
            "urlOnline" to urlOnline

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

    suspend fun setResource(resource: Resource): Unit = suspendCancellableCoroutine{setResourceContinuation ->

        nelsDB
            .document("resources/${resource.isbn}")
            .set(resource)
            .addOnCompleteListener {setResource ->

                if(setResource.isSuccessful){
                    setResourceContinuation.resume(Unit)
                } else {
                    setResourceContinuation.resumeWithException(
                        FirebaseSetResourceException(
                            setResource.exception?.message.toString()
                        )
                    )
                }

            }

    }


    //LIBRARY METHODS
    suspend fun addLibrary(nombre: String, direccion: String, geopoint: GeoPoint): Unit = suspendCancellableCoroutine{addLibraryContinuation ->

        val newLibrary: HashMap<String, Any> = hashMapOf(
            "name" to nombre,
            "address" to direccion,
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
                        inputLibrary.id = library.id
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

    suspend fun getLibrary(id: String): Library = suspendCancellableCoroutine{getLibraryContinuation ->

        nelsDB
            .collection("libraries")
            .document(id)
            .get()
            .addOnCompleteListener {library ->

                if(library.isSuccessful){

                    val libraryDB = library.getResult()!!.toObject(Library::class.java)
                    libraryDB?.id = library.getResult()!!.id
                    getLibraryContinuation.resume(libraryDB!!)

                } else {

                    getLibraryContinuation.resumeWithException(FirebaseGetLibraryException(library.exception?.message.toString()))

                }


            }


    }

}