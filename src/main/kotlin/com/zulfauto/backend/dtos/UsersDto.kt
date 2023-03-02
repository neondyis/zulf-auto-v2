package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.Role
import com.zulfauto.backend.models.Users
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.Users} entity
 */
data class UsersDto(
    val user: Users? = null,
    val role: Role? = null,
) : Serializable