package com.sinergia.eLibrary.data.NeLS_DataBase

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.sinergia.eLibrary.base.Exceptions.FirebaseStorageGetDownloadURIException
import com.sinergia.eLibrary.base.Exceptions.FirebaseStorageUploadImageException
import kotlinx.coroutines.suspendCancellableCoroutine
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

}