package com.sinergia.eLibrary.data.Model

import java.time.LocalDateTime

class Reserve (

    var userMail: String = "Desconocido",
    var resourceId: String = "Desconocido",
    var resourceName: String = "Desconocido",
    var libraryId: String = "Desconocido",
    var reserveDate: String = "",
    var loanDate: String = "",
    var status: String = "Pending",
    var id: String = "No id"

)