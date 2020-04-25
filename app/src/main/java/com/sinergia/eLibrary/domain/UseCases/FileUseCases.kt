package com.sinergia.eLibrary.domain.UseCases

import android.net.Uri
import com.sinergia.eLibrary.data.NeLS_DataBase.NelsStorage

class FileUseCases {

    val nelsStorage = NelsStorage()

    suspend fun uploadImage(owner: String, imageURI: Uri): Uri{
        return nelsStorage.uploadImage(owner, imageURI)
    }

}