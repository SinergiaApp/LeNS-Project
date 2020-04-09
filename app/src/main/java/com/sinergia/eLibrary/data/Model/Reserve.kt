package com.sinergia.eLibrary.data.Model

import java.time.LocalDateTime

class Reserve (

    var id: String = "No id",
    var userMail: String = "Desconocido",
    var resourceId: String = "Desconocido",
    var resourceName: String = "Desconocido",
    var libraryId: String = "Desconocido",
    var reserveDate: LocalDateTime ?= null,
    var loanDate: LocalDateTime ?= null,
    var status: String = "Pending"

)