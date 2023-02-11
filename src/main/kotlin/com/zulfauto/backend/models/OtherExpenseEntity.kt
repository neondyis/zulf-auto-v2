package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "other_expense", schema = "public", catalog = "postgres")
open class OtherExpenseEntity {
    @get:Id
    @get:Column(name = "id", nullable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "car", nullable = false, insertable = false, updatable = false)
    var car: Int? = null

    @get:Basic
    @get:Column(name = "name", nullable = false)
    var name: String? = null

    @get:Basic
    @get:Column(name = "cost", nullable = false)
    var cost: java.math.BigInteger? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "car", referencedColumnName = "id")
    var refCarEntity: CarEntity? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "car = $car " +
                "name = $name " +
                "cost = $cost " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as OtherExpenseEntity

        if (id != other.id) return false
        if (car != other.car) return false
        if (name != other.name) return false
        if (cost != other.cost) return false

        return true
    }

}

