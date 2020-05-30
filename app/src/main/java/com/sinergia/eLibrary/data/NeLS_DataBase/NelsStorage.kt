package com.sinergia.eLibrary.data.NeLS_DataBase

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sinergia.eLibrary.base.Exceptions.*
import com.sinergia.eLibrary.data.Model.Article
import kotlinx.coroutines.suspendCancellableCoroutine
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class NelsStorage {

    val nelsStorage = FirebaseStorage.getInstance().getReference()

    suspend fun uploadImage(owner: String, imageURI: Uri): Uri = suspendCancellableCoroutine{uploadImageContinuation ->

        nelsStorage
            .child("images/$owner")
            .downloadUrl
            .addOnCompleteListener { getDownLoadURI ->

            if(getDownLoadURI.isSuccessful){

                nelsStorage
                    .child("images/$owner")
                    .putFile(imageURI)
                    .addOnCompleteListener { uploadImage ->

                        if(uploadImage.isSuccessful){

                            uploadImageContinuation.resume(getDownLoadURI.result!!)

                        } else {
                            uploadImageContinuation.resumeWithException(
                                FirebaseStorageUploadImageException(uploadImage.exception?.message.toString())
                            )
                        }

                    }

            } else {
                uploadImageContinuation.resumeWithException(
                    FirebaseStorageGetDownloadURIException(getDownLoadURI.exception?.message.toString())
                )
            }

        }

    }

    suspend fun uploadLibraryImage(libraryId: String, libraryImageURI: Uri): Uri = suspendCancellableCoroutine { uploadLibraryImageContinuation ->

        nelsStorage
            .child("libraryImages/$libraryId")
            .putFile(libraryImageURI)
            .addOnCompleteListener {uploadLibraryImage ->

                if(uploadLibraryImage.isSuccessful){

                    nelsStorage
                        .child("libraryImages/$libraryId")
                        .downloadUrl
                        .addOnCompleteListener { getLibraryImageURI ->

                            if(getLibraryImageURI.isSuccessful){

                                uploadLibraryImageContinuation.resume(getLibraryImageURI.result!!)

                            } else {

                                uploadLibraryImageContinuation.resumeWithException(
                                    FirebaseStorageGetDownloadURIException(getLibraryImageURI.exception?.message.toString())
                                )

                            }

                        }

                } else {

                    uploadLibraryImageContinuation.resumeWithException(
                        FirebaseStorageUploadImageException(uploadLibraryImage.exception?.message.toString())
                    )

                }

            }

    }

    suspend fun uploadResourceImage(resourceId: String, resourceImageURI: Uri): Uri = suspendCancellableCoroutine { uploarResourceImageContinuation ->

        nelsStorage
            .child("resourceImages/$resourceId")
            .putFile(resourceImageURI)
            .addOnCompleteListener { uploadResourceImage ->

                if (uploadResourceImage.isSuccessful) {

                    nelsStorage
                        .child("resourceImages/$resourceId")
                        .downloadUrl
                        .addOnCompleteListener { getResourceImageURI ->

                            if (getResourceImageURI.isSuccessful) {

                                uploarResourceImageContinuation.resume(getResourceImageURI.result!!)

                            } else {

                                uploarResourceImageContinuation.resumeWithException(
                                    FirebaseStorageGetDownloadURIException(getResourceImageURI.exception?.message.toString())
                                )

                            }

                        }

                } else {

                    uploarResourceImageContinuation.resumeWithException(
                        FirebaseStorageUploadImageException(uploadResourceImage.exception?.message.toString())
                    )

                }

            }
    }

    suspend fun uploadArticle(articleId: String, articleUri: Uri): Uri = suspendCancellableCoroutine { uploadArticleContinuation ->

        nelsStorage
            .child("articles/$articleId")
            .putFile(articleUri)
            .addOnCompleteListener { uploadArticle ->

                if(uploadArticle.isSuccessful){

                    nelsStorage
                        .child("resourceImages/$articleId")
                        .downloadUrl
                        .addOnCompleteListener{ downloadUri ->

                            if(downloadUri.isSuccessful){
                                uploadArticleContinuation.resume(downloadUri.result!!)
                            } else {
                                uploadArticleContinuation.resumeWithException(
                                    FirebaseStorageGetDownloadURIException(
                                        downloadUri.exception?.message.toString()
                                    )
                                )
                            }

                        }

                } else {
                    uploadArticleContinuation.resumeWithException(
                        FirebaseStorageUploadArticleException(
                            uploadArticle.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun downloadArticle(article: Article): Unit = suspendCancellableCoroutine { downloadArticleContinuation ->

        var localFile: File = File.createTempFile("documents", "pdf")

        nelsStorage
            .child("articles/${article.id}")
            .getFile(localFile)
            .addOnCompleteListener{ downloadArticle ->

                if(downloadArticle.isSuccessful){
                    downloadArticleContinuation.resume(Unit)
                } else {
                    downloadArticleContinuation.resumeWithException(
                        FirebaseStorageDownload(
                            downloadArticle.exception?.message.toString()
                        )
                    )
                }

            }

    }

    suspend fun deleteArticle(articleId: String): Unit = suspendCancellableCoroutine { deleteArticleContinuation ->

        nelsStorage
            .child("articles/$articleId")
            .delete()
            .addOnCompleteListener { deleteArticle ->

                if(deleteArticle.isSuccessful){
                    deleteArticleContinuation.resume(Unit)
                } else {
                    deleteArticleContinuation.resumeWithException(
                        FirebaseStorageDeleteException(
                            deleteArticle.exception?.message.toString()
                        )
                    )
                }

            }

    }

}