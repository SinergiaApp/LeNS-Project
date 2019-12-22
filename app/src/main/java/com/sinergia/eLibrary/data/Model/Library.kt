package com.sinergia.eLibrary.data.Model

import com.google.firebase.firestore.GeoPoint

data class Library (

    var name: String = "Desconcido",
    var address: String = "Desconocida",
    var geopoint: GeoPoint = GeoPoint(0.0, 0.0)

)