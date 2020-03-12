package com.sinergia.eLibrary.data.Model

import java.time.LocalDateTime

class Reserve (

    var userMail: String = "Desconocido",
    var resourceId: String = "Desconocido",
    var libraryId: String = "Desconocido",
    var reserveDate: LocalDateTime,
    var loanDate: LocalDateTime

)