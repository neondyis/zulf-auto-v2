package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "role_permission", schema = "public", catalog = "postgres")
open class RolePermissionEntity {
    @get:Id
    @get:Column(name = "id", nullable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "name", nullable = false)
    var name: String? = null

    @get:Basic
    @get:Column(name = "role", nullable = false, insertable = false, updatable = false)
    var role: Int? = null

    @get:Basic
    @get:Column(name = "granted", nullable = false)
    var granted: Boolean? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "role", referencedColumnName = "id")
    var refRoleEntity: RoleEntity? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "name = $name " +
                "role = $role " +
                "granted = $granted " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as RolePermissionEntity

        if (id != other.id) return false
        if (name != other.name) return false
        if (role != other.role) return false
        if (granted != other.granted) return false

        return true
    }

}

