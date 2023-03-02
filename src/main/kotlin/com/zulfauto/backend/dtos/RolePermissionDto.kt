package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.Role
import com.zulfauto.backend.models.RolePermission
import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.RolePermission} entity
 */
data class RolePermissionDto(
    val rolePermission: RolePermission? = null,
    @field:NotNull val role: Role? = null,
) : Serializable