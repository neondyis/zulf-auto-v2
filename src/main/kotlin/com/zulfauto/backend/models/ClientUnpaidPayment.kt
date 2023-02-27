package com.zulfauto.backend.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import org.springframework.data.annotation.Id

@Table(name = "client_unpaid_payment")
open class ClientUnpaidPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "client_payment", nullable = false)
    open var clientPayment: Int? = null

    @NotNull
    @Column(name = "amount", nullable = false)
    open var amount: BigDecimal? = null

    @NotNull
    @Column(name = "amount_paid", nullable = false)
    open var amount_paid: BigDecimal? = null

    @NotNull
    @Column(name = "date", nullable = false)
    open var date: LocalDate? = null

    @NotNull
    @Column(name = "paid", nullable = false)
    open var paid: Boolean? = false
}