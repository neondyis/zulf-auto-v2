package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "expense", schema = "public", catalog = "postgres")
open class ExpenseEntity {
    @get:Id
    @get:Column(name = "id", nullable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "fuel", nullable = true)
    var fuel: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "shipping", nullable = true)
    var shipping: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "trailer", nullable = true)
    var trailer: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "import_tax", nullable = true)
    var importTax: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "travel_cost", nullable = true)
    var travelCost: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "car", nullable = true, insertable = false, updatable = false)
    var car: Int? = null

    @get:Basic
    @get:Column(name = "documentation", nullable = true)
    var documentation: java.math.BigInteger? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "car", referencedColumnName = "id")
    var refCarEntity: CarEntity? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "fuel = $fuel " +
                "shipping = $shipping " +
                "trailer = $trailer " +
                "importTax = $importTax " +
                "travelCost = $travelCost " +
                "car = $car " +
                "documentation = $documentation " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ExpenseEntity

        if (id != other.id) return false
        if (fuel != other.fuel) return false
        if (shipping != other.shipping) return false
        if (trailer != other.trailer) return false
        if (importTax != other.importTax) return false
        if (travelCost != other.travelCost) return false
        if (car != other.car) return false
        if (documentation != other.documentation) return false

        return true
    }

}

