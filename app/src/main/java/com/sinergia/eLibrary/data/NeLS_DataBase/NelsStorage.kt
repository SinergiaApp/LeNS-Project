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

}