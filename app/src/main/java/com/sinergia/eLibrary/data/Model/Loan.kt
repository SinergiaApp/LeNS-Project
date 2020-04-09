package com.sinergia.eLibrary.data.Model

import java.time.LocalDateTime

data class Loan(

    var id: String = "No id",
    var userMail: String = "Desconocido",
    var resourceId: String = "Desconocido",
    var resourceName: String = "Desconocido",
    var libraryId: String = "Desconocido",
    var loanDate: LocalDateTime,
    var returnDate: LocalDateTime,
    var status: String = "Pending"

    )