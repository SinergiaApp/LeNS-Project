package com.sinergia.eLibrary.data.Model

import java.time.LocalDateTime

data class Loan(

    var userMail: String = "Desconocido",
    var resourceId: String = "Desconocido",
    var resourceName: String = "Desconocido",
    var libraryId: String = "Desconocido",
    var loanDate: String = "",
    var returnDate: String = "",
    var status: String = "Pending",
    var id: String = "No id"

)