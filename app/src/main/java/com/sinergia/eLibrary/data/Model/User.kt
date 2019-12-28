package com.sinergia.eLibrary.data.Model

data class User(

    var name: String = "Desconocido",
    var lastName: String = "Desconocido",
    var email: String = "Desconocido",
    var admin: Boolean = false,
    var resources: HashMap<String, String> = hashMapOf()

)