package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "car_feature", schema = "public", catalog = "postgres")
open class CarFeatureEntity {
    @get:Id
    @get:Column(name = "id", nullable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "feature", nullable = false, insertable = false, updatable = false)
    var feature: Int? = null

    @get:Basic
    @get:Column(name = "car", nullable = false, insertable = false, updatable = false)
    var car: Int? = null

    @get:Basic
    @get:Column(name = "value", nullable = true)
    var value: String? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "feature", referencedColumnName = "id")
    var refFeatureEntity: FeatureEntity? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "car", referencedColumnName = "id")
    var refCarEntity: CarEntity? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "feature = $feature " +
                "car = $car " +
                "value = $value " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CarFeatureEntity

        if (id != other.id) return false
        if (feature != other.feature) return false
        if (car != other.car) return false
        if (value != other.value) return false

        return true
    }

}

