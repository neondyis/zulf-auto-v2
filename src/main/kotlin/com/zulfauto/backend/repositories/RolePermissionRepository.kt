package com.zulfauto.backend.repositories;

import com.zulfauto.backend.models.RolePermission
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface RolePermissionRepository : R2dbcRepository<RolePermission, Int> {
}