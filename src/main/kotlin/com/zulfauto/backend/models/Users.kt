package com.zulfauto.backend.models

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id
import org.springframework.security.core.GrantedAuthority


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
    @JsonIgnore
    open var password: String? = null

    @NotNull
    @Column(name = "email", nullable = false, length = Integer.MAX_VALUE)
    open var email: String? = null

    @Column(name = "role")
    @JsonIgnore
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