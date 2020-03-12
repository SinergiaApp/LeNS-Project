package com.sinergia.eLibrary.data.Model

data class User(

    var name: String = "Desconocido",
    var lastName1: String = "Desconocido",
    var lastName2: String = "Desconocido",
    var email: String = "Desconocido",
    var nif: String = "Desconocido",
    var reserves: List<String> = emptyList(),
    var loans: List<String> = emptyList(),
    var favorites: List<String> = emptyList(),
    var admin: Boolean = false

)