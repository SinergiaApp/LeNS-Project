package com.sinergia.eLibrary.domain.UseCases

import android.net.Uri
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsStorage

class FileUseCases {

    val nelsStorage = NelsStorage()

    suspend fun uploadImage(owner: String, imageURI: Uri): Uri{
        return nelsStorage.uploadImage(owner, imageURI)
    }

    suspend fun uploadLibraryImage(libraryId: String, libraryImageURI: Uri): Uri{
        return nelsStorage.uploadLibraryImage(libraryId, libraryImageURI)
    }

    suspend fun uploadResourceImage(resourceId: String, resourceImageURI: Uri): Uri{
        return nelsStorage.uploadLibraryImage(resourceId, resourceImageURI)
    }

}