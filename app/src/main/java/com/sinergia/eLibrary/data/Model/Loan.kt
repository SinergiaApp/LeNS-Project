package com.sinergia.eLibrary.data.Model

import java.time.LocalDateTime

data class Loan(

    var userMail: String = "Desconocido",
    var idResource: String = "Desconocido",
    var lbrary: String = "Desconocido",
    var loanDate: LocalDateTime ?= null,
    var returnDate: LocalDateTime ?= null

    )