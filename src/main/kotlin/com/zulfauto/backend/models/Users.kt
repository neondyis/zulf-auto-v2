package com.zulfauto.backend.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotNull
import org.springframework.security.core.GrantedAuthority
import org.springframework.data.annotation.Id

@Table(name = "users")
open class Users : GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "username", nullable = false, length = Integer.MAX_VALUE)
    open var username: String? = null

    @NotNull
    @Column(name = "password", nullable = false, length = Integer.MAX_VALUE)
    open var password: String? = null

    @NotNull
    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    open var email: String? = null

    @Column(name = "role")
    open var role: Int? = null

    @NotNull
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    open var name: String? = null

    override fun getAuthority(): String {
        return when (role) {
            1 -> "Reader"
            2 -> "User"
            3 -> "Maintainer"
            4 -> "Admin"
            else -> {
                "None"
            }
        }
    }
}