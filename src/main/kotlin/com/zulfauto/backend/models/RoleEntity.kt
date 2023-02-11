package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "role", schema = "public", catalog = "postgres")
open class RoleEntity {
    @get:Id
    @get:Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "name", nullable = false)
    var name: String? = null

    @get:OneToMany(mappedBy = "refRoleEntity")
    var refRolePermissionEntities: List<RolePermissionEntity>? = null

    @get:OneToMany(mappedBy = "refRoleEntity")
    var refUserEntities: List<UserEntity>? = null

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
        other as RoleEntity

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

}

