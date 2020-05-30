package com.sinergia.eLibrary.data.Model

import com.google.firebase.firestore.GeoPoint

data class Article (

    var title: String = "Desconcido",
    var authors: List<String> = emptyList(),
    var year: Int = 0,
    var source: String = "Desconcido",
    var issn: String = "Desconcido",
    var descriptiom: String = "Desconcido",
    var downloadURI: String = "Desconcido",
    var category: String = "Desconocido",
    var id: String = "Identificador Desconocido"

)