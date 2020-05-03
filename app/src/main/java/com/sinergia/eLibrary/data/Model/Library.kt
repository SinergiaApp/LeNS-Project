package com.sinergia.eLibrary.data.Model

import com.google.firebase.firestore.GeoPoint

data class Library (

    var id: String = "Identificador Desconocido",
    var name: String = "Nombre Desconcido",
    var address: String = "Dirección Desconocida",
    var geopoint: GeoPoint = GeoPoint(0.0, 0.0),
    var imageUri: String = "noImage"

)