package com.zulfauto.backend.repositories;

import com.zulfauto.backend.models.Role
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface RoleRepository : R2dbcRepository<Role, Int> {
}