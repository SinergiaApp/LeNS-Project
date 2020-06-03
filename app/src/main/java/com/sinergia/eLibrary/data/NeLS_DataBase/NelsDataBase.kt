package com.sinergia.eLibrary.data.NeLS_DataBase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.sinergia.eLibrary.base.Exceptions.*
import com.sinergia.eLibrary.base.Exceptions.FirebaseAddUserException
import com.sinergia.eLibrary.data.Model.*
import com.sinergia.eLibrary.presentation.NeLSProject
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDateTime
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NelsDataBase {

    val nelsDB= FirebaseFirestore.getInstance()

    //USERS METHODS
    suspend fun addUser(newUser: User): Unit = suspendCancellableCoroutine{addUserContinuation ->

        val newUserDB: HashMap<String, Any> = hashMapOf(
            "name" to newUser.name,
            "lastName1" to newUser.lastName1,
            "lastName2" to newUser.lastName2,
            "email" to newUser.email,
            "nif" to  newUser.nif,
            "reserves" to newUser.reserves,
            "loans" to newUser.loans,
            "favorites" to newUser.favorites,
            "admin" to newUser.admin,
            "researcher" to newUser.researcher,
            "avatar" to newUser.avatar
        )

        nelsDB
            .document("users/${newUser.email}")
            .set(newUserDB)
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

    suspend fun getUser(email: String): User = suspendCancellableCoroutine{getUserContinue ->

        nelsDB
            .collection("users")
            .document(email)
            .get()
            .addOnCompleteListener {user ->
                if(user.isSuccessful){
                    val currentUser = user.result!!.toObject(User::class.java)
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
                            FirebaseSetUserException(
                                setUser.exception?.message.toString()
                            )
                        )
                    }

                }
    }

    suspend fun deleteUser(user: User): Unit = suspendCancellableCoroutine { deleteUserContinuation ->

        nelsDB
            .collection("users")
            .document(user.email)
            .delete()
            .addOnCompleteListener { deleteUser ->

                if (deleteUser.isSuccessful) {
                    deleteUserContinuation.resume(Unit)
                } else {

                    deleteUserContinuation.resumeWithException(
                        FirebaseDeleteUserException(deleteUser.exception?.message.toString())
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

                    for (resource in resources.result!!){
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

    suspend fun getFavouriteResources(email: String): List<Resource> = suspendCancellableCoroutine { getFavouriteResourcesContinuation ->

        var resourcesList: ArrayList<Resource> = arrayListOf()

        nelsDB
            .collection("resources")
            .whereArrayContains("likes", email)
            .get()
            .addOnCompleteListener{getFavouriteResources ->

                if(getFavouriteResources.isSuccessful){

                    for (resource in getFavouriteResources.result!!){
                        val inputResource: Resource = resource.toObject(Resource::class.java)
                        resourcesList.add(inputResource)
                    }
                    getFavouriteResourcesContinuation.resume(resourcesList)
                } else {
                    getFavouriteResourcesContinuation.resumeWithException(
                        FirebaseGetAllResourcesException(getFavouriteResources.exception?.message.toString())
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
                    getResourceContinuation.resume(resourceDB)
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

    suspend fun getFavouriteResource(isbn: String): Resource = suspendCancellableCoroutine { getFavouriteResourceContinuation ->

        nelsDB
            .collection("resources")
            .document(isbn)
            .get()
            .addOnCompleteListener { getFavouriteResource ->

                if(getFavouriteResource.isSuccessful){

                    val resourceDB = getFavouriteResource.result?.toObject(Resource::class.java)

                    if(resourceDB == null || !resourceDB.likes.contains(NeLSProject.currentUser.email)) {
                        getFavouriteResourceContinuation.resumeWithException(
                            FirebaseGetResourceException(
                                "Vaya... el Recurso con ISBN $isbn no está en tu lista de favoritos."
                            )
                        )
                    } else {
                        getFavouriteResourceContinuation.resume(resourceDB)
                    }

                } else {

                    getFavouriteResourceContinuation.resumeWithException(
                        FirebaseGetResourceException(
                            getFavouriteResource.exception?.message.toString()
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
        disponibility: MutableMap<String, Int>,
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
            "urlOnline" to urlOnline,
            "imageUri" to "noImage"

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

    suspend fun deleteResource(deletedResource: Resource): Unit = suspendCancellableCoroutine { deleteResourceContinuation ->

        nelsDB
            .collection("resources")
            .document(deletedResource.isbn)
            .delete()
            .addOnCompleteListener { deleteResource ->

                if(deleteResource.isSuccessful){
                    deleteResourceContinuation.resume(Unit)
                } else {
                    deleteResourceContinuation.resumeWithException(
                        FirebaseDeleteResourceException(
                            deleteResource.exception?.message.toString()
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
            "geopoint" to geopoint,
            "imageUri" to "noImage"
        )

        nelsDB
            .collection("libraries")
            .add(newLibrary)
            .addOnCompleteListener {addLibrary ->
                if(addLibrary.isSuccessful){
                    addLibraryContinuation.resume(Unit)
                } else {
                    addLibraryContinuation.resumeWithException(
                        FirebaseCreateLibraryException(
                            addLibrary.exception?.message.toString()
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

                    for (library in libraries.result!!){

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
            .get().addOnCompleteListener{ getLibrary ->

                if(getLibrary.isSuccessful){

                    val libraryDB = getLibrary.result!!.toObject(Library::class.java)

                    if(libraryDB == null){
                        getLibraryContinuation.resumeWithException(
                            FirebaseGetLibraryException(
                                "Vaya... no tenemos la Biblioteca con Identificador $id."
                            )
                        )
                    } else {
                        libraryDB.id = getLibrary.result!!.id
                        getLibraryContinuation.resume(libraryDB)
                    }

                } else {
                    getLibraryContinuation.resumeWithException(
                        FirebaseGetLibraryException(
                            "Vaya... no tenemos la Biblioteca con Identificador $id."
                        )
                    )
                }

            }

    }

    suspend fun setLibrary(library: Library): Unit = suspendCancellableCoroutine{setLibraryContinuation ->

        val settedLibrary: HashMap<String, Any> = hashMapOf(
            "name" to library.name,
            "address" to library.address,
            "geopoint" to library.geopoint,
            "imageUri" to library.imageUri
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

    suspend fun deleteLibrary(deletedLibrary: Library): Unit = suspendCancellableCoroutine { deleteLibraryContinuation ->

        nelsDB
            .collection("libraries")
            .document(deletedLibrary.id)
            .delete()
            .addOnCompleteListener { deleteLibrary ->

                if(deleteLibrary.isSuccessful){
                    deleteLibraryContinuation.resume(Unit)
                } else {
                    deleteLibraryContinuation.resumeWithException(
                        FirebaseDeleteLibraryException(
                            deleteLibrary.exception?.message.toString()
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

                    for (userPendingReserve in getUserPendingReserves.result!!) {

                        val inputUserPendingReserve = userPendingReserve.toObject(Reserve::class.java)
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
            "reserveDate" to reserve.reserveDate,
            "loanDate" to reserve.loanDate,
            "status" to reserve.status
        )

        nelsDB
            .document("reserves/${reserve.userMail+reserve.resourceId+reserve.reserveDate}")
            .set(newReserve)
            .addOnCompleteListener {addReserve ->
                if(addReserve.isSuccessful){
                    addReserveContinuation.resume(Unit)
                } else {
                    addReserveContinuation.resumeWithException(
                        FirebaseAddReserveException(
                            addReserve.exception?.message.toString()
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
            "reserveDate" to reserve.reserveDate,
            "loanDate" to reserve.loanDate,
            "status" to reserve.status
        )

        nelsDB
            .document("reserves/${reserve.userMail+reserve.resourceId+reserve.reserveDate}")
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

                    for (userPendingLoan in userPendingLoans.result!!){

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
            "resourceName" to loan.resourceName,
            "libraryId" to loan.libraryId,
            "loanDate" to loan.loanDate,
            "returnDate" to loan.returnDate,
            "status" to loan.status
        )

        nelsDB
            .document("loans/${loan.userMail+loan.resourceId+loan.loanDate}")
            .set(newLoan)
            .addOnCompleteListener {addLoan ->
                if(addLoan.isSuccessful){
                    addLoanContination.resume(Unit)
                } else {
                    addLoanContination.resumeWithException(
                        FirebaseAddLoanException(
                            addLoan.exception?.message.toString()
                        )
                    )
                }
            }

    }

    suspend fun setLoan(loan: Loan): Unit = suspendCancellableCoroutine { setLoanContinuation ->

        val settedLoan: HashMap<String, Any> = hashMapOf(
            "userMail" to loan.userMail,
            "resourceId" to loan.resourceId,
            "resourceName" to loan.resourceName,
            "libraryId" to loan.libraryId,
            "loanDate" to loan.loanDate,
            "returnDate" to loan.returnDate,
            "status" to loan.status
        )

        nelsDB
            .document("loans/${loan.userMail+loan.resourceId+loan.loanDate}")
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


    // ARTICLE MEHTODS
    suspend fun getAllArticles(): ArrayList<Article> = suspendCancellableCoroutine { getAllArticlesContunuation ->

        var articlesList = arrayListOf<Article>()

        nelsDB
            .collection("articles")
            .get()
            .addOnCompleteListener {getAllArticles ->

                if(getAllArticles.isSuccessful){

                    for (article in getAllArticles.result!!){

                        val inputArticle = article.toObject(Article::class.java)
                        inputArticle.id = article.id
                        articlesList.add(inputArticle)

                    }

                    getAllArticlesContunuation.resume(articlesList)

                } else {
                    getAllArticlesContunuation.resumeWithException(
                        FirebaseGetAllArticlesException(
                            getAllArticles.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun getAllArticlesWithCategory(category: String): ArrayList<Article> = suspendCancellableCoroutine { getAllArticlesContunuation ->

        var articlesList = arrayListOf<Article>()

        nelsDB
            .collection("articles")
            .whereEqualTo("category", category)
            .get()
            .addOnCompleteListener {getAllArticles ->

                if(getAllArticles.isSuccessful){

                    for (article in getAllArticles.result!!){

                        val inputArticle = article.toObject(Article::class.java)
                        inputArticle.id = article.id
                        articlesList.add(inputArticle)

                    }

                    getAllArticlesContunuation.resume(articlesList)

                } else {
                    getAllArticlesContunuation.resumeWithException(
                        FirebaseGetAllArticlesException(
                            getAllArticles.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun newArticle(newArticle: Article): Unit = suspendCancellableCoroutine { newArticleContinuation ->

        nelsDB
            .document("articles/${newArticle.id}")
            .set(newArticle)
            .addOnCompleteListener { addArticle ->

                if(addArticle.isSuccessful){
                    newArticleContinuation.resume(Unit)
                } else {
                    newArticleContinuation.resumeWithException(
                        FirebaseAddArticleException(
                            addArticle.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun setArticle(settedArticle: Article): Unit = suspendCancellableCoroutine { setArticleContinuation ->

        nelsDB
            .document("articles/${settedArticle.id}")
            .set(settedArticle)
            .addOnCompleteListener { setArticle ->

                if(setArticle.isSuccessful){
                    setArticleContinuation.resume(Unit)
                } else {
                    setArticleContinuation.resumeWithException(
                        FirebaseSetArticleException(
                            setArticle.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun deleteArticle(article: Article): Unit= suspendCancellableCoroutine { deleteArticleContinuation ->

        nelsDB
            .collection("articles")
            .document(article.id)
            .delete()
            .addOnCompleteListener {deleteArticle ->

                if(deleteArticle.isSuccessful){
                    deleteArticleContinuation.resume(Unit)
                } else {
                    deleteArticleContinuation.resumeWithException(
                        FirebaseDeleteArticleException(
                            deleteArticle.exception?.message.toString()
                        )
                    )
                }


            }

    }

}