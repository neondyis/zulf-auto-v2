package com.zulfauto.backend.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal
import org.springframework.data.annotation.Id

@Table(name = "other_expense")
open class OtherExpense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "car", nullable = false)
    open var car: Int? = null

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    open var name: String? = null

    @NotNull
    @Column(name = "cost", nullable = false)
    open var cost: BigDecimal? = null
}