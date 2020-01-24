package com.sinergia.eLibrary.data.Model

data class User(

    var name: String = "Desconocido",
    var lastName1: String = "Desconocido",
    var lastName2: String = "Desconocido",
    var email: String = "Desconocido",
    var nif: String = "Desconocido",
    var loans: MutableList<String> = mutableListOf(),
    var favorites: MutableList<String> = mutableListOf(),
    var admin: Boolean = false

)