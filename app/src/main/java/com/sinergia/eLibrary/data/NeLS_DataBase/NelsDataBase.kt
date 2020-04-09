package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.base.Exceptions.*
import com.sinergia.eLibrary.base.Exceptions.FirebaseAddUserException
import com.sinergia.eLibrary.data.Model.*
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

    suspend fun setUser(settedUser: User): Unit = suspendCancellableCoroutine{ setUserContinuation ->

        nelsDB
            .document("users/${settedUser.email}")
                .set(settedUser)
                .addOnCompleteListener {setUser ->

                    if(setUser.isSuccessful){
                        setUserContinuation.resume(Unit)
                    } else {
                        setUserContinuation.resumeWithException(
                            FirebaseSetLoanException(
                                setUser.exception?.message.toString()
                            )
                        )
                    }

                }
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
            .addOnSuccessListener {resource ->
                val resourceDB = resource.toObject(Resource::class.java)
                if(resourceDB == null){
                    getResourceContinuation.resumeWithException(
                        FirebaseGetResourceException(
                            "Vaya... no tenemos el Recurso con ISBN $isbn."
                        )
                    )
                } else {
                    getResourceContinuation.resume(resourceDB!!)
                }
            }
            .addOnFailureListener {resourceException ->
                getResourceContinuation.resumeWithException(
                    FirebaseGetResourceException(
                        resourceException.message.toString()
                    )
                )
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
            .addOnSuccessListener { library ->

                val libraryDB = library.toObject(Library::class.java)

                if(libraryDB == null){
                    getLibraryContinuation.resumeWithException(
                        FirebaseGetLibraryException(
                            "Vaya... no tenemos la Biblioteca con Identificador $id."
                        )
                    )
                } else {
                    libraryDB?.id = library.id
                    getLibraryContinuation.resume(libraryDB!!)
                }

            }
            .addOnFailureListener{getLibraryException ->
                getLibraryContinuation.resumeWithException(
                    FirebaseGetLibraryException(
                        "Vaya... no tenemos la Biblioteca con Identificador $id."
                    )
                )

            }

    }

    suspend fun setLibrary(library: Library): Unit = suspendCancellableCoroutine{setLibraryContinuation ->

        val settedLibrary: HashMap<String, Any> = hashMapOf(
            "name" to library.name,
            "address" to library.address,
            "geopoint" to library.geopoint
        )

        nelsDB
            .document("libraries/${library.id}")
            .set(settedLibrary)
            .addOnCompleteListener {setLibrary ->

                if(setLibrary.isSuccessful){
                    setLibraryContinuation.resume(Unit)
                } else {
                    setLibraryContinuation.resumeWithException(
                        FirebaseSetResourceException(
                            setLibrary.exception?.message.toString()
                        )
                    )
                }

            }

    }

    // RESERVE METHODS
    suspend fun getUserPendingReserves(email: String): ArrayList<Reserve> = suspendCancellableCoroutine { getUserPendingReservesContinue ->

        var userPendingReservesList = arrayListOf<Reserve>()
        nelsDB
            .collection("reserves")
            .whereEqualTo("userMail", email)
            .whereEqualTo("status", "Pending")
            .get()
            .addOnCompleteListener { getUserPendingReserves ->

                if(getUserPendingReserves.isSuccessful){

                    for (userPendingReserve in getUserPendingReserves.getResult()!!){

                        val inputUserPendingReserve = userPendingReserve.toObject(Reserve::class.java)
                        inputUserPendingReserve.id = userPendingReserve.id
                        userPendingReservesList.add(inputUserPendingReserve)

                    }

                    getUserPendingReservesContinue.resume(userPendingReservesList)

                } else {

                    getUserPendingReservesContinue.resumeWithException(
                        FirebaseGetUserReservesException(
                            getUserPendingReserves.exception?.message.toString()
                        )
                    )

                }

            }

    }

    suspend fun newReserve(reserve: Reserve): Unit = suspendCancellableCoroutine{ addReserveContinuation ->

        val newReserve: HashMap<String, Any> = hashMapOf(
            "userMail" to reserve.userMail,
            "resourceId" to reserve.resourceId,
            "resourceName" to reserve.resourceName,
            "libraryId" to reserve.libraryId,
            "reserveDate" to reserve.reserveDate!!,
            "status" to reserve.status
        )
        if(reserve.loanDate !== null) newReserve["loanDate"] = reserve.loanDate!!

        nelsDB
            .document("reserves/${reserve.userMail+reserve.resourceId}")
            .set(newReserve)
            .addOnCompleteListener {newReserve ->
                if(newReserve.isSuccessful){
                    addReserveContinuation.resume(Unit)
                } else {
                    addReserveContinuation.resumeWithException(
                        FirebaseAddReserveException(
                            newReserve.exception?.message.toString()
                        )
                    )
                }
            }

    }

    suspend fun setReserve(reserve: Reserve): Unit = suspendCancellableCoroutine { setReserveLoanDateContinuation ->

        val settedReserve: HashMap<String, Any> = hashMapOf(
            "userMail" to reserve.userMail,
            "resourceId" to reserve.resourceId,
            "resourceName" to reserve.resourceName,
            "libraryId" to reserve.libraryId,
            "reserveDate" to reserve.reserveDate!!,
            "loanDate" to reserve.loanDate!!
        )

        nelsDB
            .document("reserves/${reserve.userMail+reserve.resourceId}")
            .set(settedReserve)
            .addOnCompleteListener {setReserve ->

                if(setReserve.isSuccessful){
                    setReserveLoanDateContinuation.resume(Unit)
                } else {
                    setReserveLoanDateContinuation.resumeWithException(
                        FirebaseSetReserveException(
                            setReserve.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun cancelReserve(cancelledReserve: String): Unit = suspendCancellableCoroutine{ cancelReserveContinue ->

        nelsDB
            .collection("reserves")
            .document(cancelledReserve)
            .delete()
            .addOnCompleteListener { cancelResource ->

                if(cancelResource.isSuccessful){
                    cancelReserveContinue.resume(Unit)
                } else {
                    cancelReserveContinue.resumeWithException(
                        FirebaseCancelReserveException(
                            cancelResource.exception?.message.toString()
                        )
                    )
                }

            }

    }

    // LOAN METHODS
    suspend fun getUserPendingLoans(email: String): ArrayList<Loan> = suspendCancellableCoroutine{ getUserPendingLoansContinuation ->

        var userPendingLoansList = arrayListOf<Loan>()

        nelsDB
            .collection("loans")
            .whereEqualTo("userMail", email)
            .whereEqualTo("status", "Pending")
            .get()
            .addOnCompleteListener {userPendingLoans ->
                if(userPendingLoans.isSuccessful){

                    for (userPendingLoan in userPendingLoans.getResult()!!){

                        val inputUserPendingLoan = userPendingLoan.toObject(Loan::class.java)
                        inputUserPendingLoan.id = userPendingLoan.id
                        userPendingLoansList.add(inputUserPendingLoan)

                    }

                    getUserPendingLoansContinuation.resume(userPendingLoansList)

                } else {

                    getUserPendingLoansContinuation.resumeWithException(
                        FirebaseGetUserLoansException(
                            userPendingLoans.exception?.message.toString()
                        )
                    )

                }
            }


    }
    suspend fun newLoan(loan: Loan): Unit = suspendCancellableCoroutine{ addLoanContination ->

        val newLoan: HashMap<String, Any> = hashMapOf(
            "userMail" to loan.userMail,
            "resourceId" to loan.resourceId,
            "libraryId" to loan.libraryId,
            "loanDate" to loan.loanDate,
            "returnDate" to loan.returnDate
        )

        nelsDB
            .document("loans/${loan.userMail+loan.resourceId}")
            .set(newLoan)
            .addOnCompleteListener {newLoan ->
                if(newLoan.isSuccessful){
                    addLoanContination.resume(Unit)
                } else {
                    addLoanContination.resumeWithException(
                        FirebaseAddLoanException(
                            newLoan.exception?.message.toString()
                        )
                    )
                }
            }

    }

    suspend fun setLoan(loan: Loan): Unit = suspendCancellableCoroutine { setLoanContinuation ->

        val settedLoan: HashMap<String, Any> = hashMapOf(
            "userMail" to loan.userMail,
            "resourceId" to loan.resourceId,
            "libraryId" to loan.libraryId,
            "loanDate" to loan.loanDate,
            "returnDate" to loan.returnDate
        )

        nelsDB
            .document("loans/${loan.userMail+loan.resourceId}")
            .set(settedLoan)
            .addOnCompleteListener {setLoan ->

                if(setLoan.isSuccessful){
                    setLoanContinuation.resume(Unit)
                } else {
                    setLoanContinuation.resumeWithException(
                        FirebaseSetLoanException(
                            setLoan.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun cancelLoan(cancelledLoan: String): Unit = suspendCancellableCoroutine { cancelLoanContinuation ->

        nelsDB
            .collection("loans")
            .document(cancelledLoan)
            .delete()
            .addOnCompleteListener { cancelLoan ->

                if(cancelLoan.isSuccessful){
                    cancelLoanContinuation.resume(Unit)
                } else {
                    cancelLoanContinuation.resumeWithException(
                        FirebaseCancelLoanException(
                            cancelLoan.exception?.message.toString()
                        )
                    )
                }

            }

    }

}