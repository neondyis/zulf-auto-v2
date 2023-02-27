package com.zulfauto.backend.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id

@Table(name = "role_permission")
open class RolePermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    open var name: String? = null

    @NotNull
    @Column(name = "role", nullable = false)
    open var role: Int? = null

    @NotNull
    @Column(name = "granted", nullable = false)
    open var granted: Boolean? = false
}