package com.zulfauto.backend.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import org.springframework.data.annotation.Id

@Table(name = "client_paid_payments")
open class ClientPaidPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "amount", nullable = false)
    open var amount: BigDecimal? = null

    @NotNull
    @Column(name = "date", nullable = false)
    open var date: LocalDate? = null

    @NotNull
    @Column(name = "client_payment", nullable = false)
    open var clientPayment: Int? = null
}