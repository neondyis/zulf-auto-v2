package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "feature", schema = "public", catalog = "postgres")
open class FeatureEntity {
    @get:Id
    @get:Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "name", nullable = false)
    var name: String? = null

    @get:OneToMany(mappedBy = "refFeatureEntity")
    var refCarFeatureEntities: List<CarFeatureEntity>? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "name = $name " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as FeatureEntity

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

}

