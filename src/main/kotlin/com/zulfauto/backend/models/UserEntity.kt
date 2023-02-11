package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "user", schema = "public", catalog = "postgres")
open class UserEntity {
    @get:Id
    @get:Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "username", nullable = false)
    var username: String? = null

    @get:Basic
    @get:Column(name = "password", nullable = false)
    var password: String? = null

    @get:Basic
    @get:Column(name = "email", nullable = false)
    var email: String? = null

    @get:Basic
    @get:Column(name = "role", nullable = true, insertable = false, updatable = false)
    var role: Int? = null

    @get:Basic
    @get:Column(name = "name", nullable = false)
    var name: String? = null

    @get:OneToMany(mappedBy = "refUserEntity")
    var refCarEntities: List<CarEntity>? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "role", referencedColumnName = "id")
    var refRoleEntity: RoleEntity? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "username = $username " +
                "password = $password " +
                "email = $email " +
                "role = $role " +
                "name = $name " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UserEntity

        if (id != other.id) return false
        if (username != other.username) return false
        if (password != other.password) return false
        if (email != other.email) return false
        if (role != other.role) return false
        if (name != other.name) return false

        return true
    }

}

