package com.sinergia.eLibrary.data.Model

import java.time.LocalDateTime

data class Loan(

    var userMail: String = "Desconocido",
    var resourceId: String = "Desconocido",
    var libraryId: String = "Desconocido",
    var loanDate: LocalDateTime,
    var returnDate: LocalDateTime

    )