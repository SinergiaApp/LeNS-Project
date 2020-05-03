package com.sinergia.eLibrary.data.Model

data class Resource(

    var title: String = "Desconocido",
    var author: List<String> = listOf("Desconocido"),
    var publisher: String = "Desconocido",
    var edition: String = "Desconocido",
    var sinopsis: String = "Desconocido",
    var isbn: String = "Desconocido",
    var disponibility: MutableMap<String, Int> = mutableMapOf(),
    var likes: MutableList<String> = mutableListOf(),
    var dislikes: MutableList<String> = mutableListOf(),
    var isOnline: Boolean = false,
    var urlOnline: String = "",
    var imageUri: String = "noImage"

)