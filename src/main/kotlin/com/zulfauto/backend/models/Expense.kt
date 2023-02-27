package com.zulfauto.backend.models

import jakarta.persistence.*
import java.math.BigDecimal
import org.springframework.data.annotation.Id

@Table(name = "expense")
open class Expense {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @Column(name = "fuel")
    open var fuel: BigDecimal? = null

    @Column(name = "shipping")
    open var shipping: BigDecimal? = null

    @Column(name = "trailer")
    open var trailer: BigDecimal? = null

    @Column(name = "import_tax")
    open var importTax: BigDecimal? = null

    @Column(name = "travel_cost")
    open var travelCost: BigDecimal? = null

    @Column(name = "car")
    open var car: Int? = null

    @Column(name = "documentation")
    open var documentation: BigDecimal? = null
}