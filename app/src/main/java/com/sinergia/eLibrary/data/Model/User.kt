package com.sinergia.eLibrary.data.Model

import android.net.Uri

data class User(

    var name: String = "Desconocido",
    var lastName1: String = "Desconocido",
    var lastName2: String = "Desconocido",
    var email: String = "Desconocido",
    var nif: String = "Desconocido",
    var reserves: MutableList<String> = mutableListOf<String>(),
    var loans: MutableList<String> = mutableListOf<String>(),
    var favorites: MutableList<String> = mutableListOf<String>(),
    var admin: Boolean = false,
    var researcher: Boolean = false,
    var avatar: String = "SinAvatar"

)