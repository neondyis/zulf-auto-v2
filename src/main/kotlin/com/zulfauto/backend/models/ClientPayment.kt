package com.zulfauto.backend.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import java.time.LocalDate
import org.springframework.data.annotation.Id

@Table(name = "client_payment")
open class ClientPayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "client", nullable = false)
    open var client: Int? = null

    @NotNull
    @Column(name = "amount", nullable = false)
    open var amount: BigDecimal? = null

    @NotNull
    @Column(name = "\"interval\"", nullable = false)
    open var interval: BigDecimal? = null

    @NotNull
    @Column(name = "start_date", nullable = false)
    open var startDate: LocalDate? = null

    @NotNull
    @Column(name = "car", nullable = false)
    open var car: Int? = null

    @Column(name = "initial_payment")
    open var initialPayment: BigDecimal? = null
}