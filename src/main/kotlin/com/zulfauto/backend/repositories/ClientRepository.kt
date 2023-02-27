package com.zulfauto.backend.repositories;

import com.zulfauto.backend.models.Client
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface ClientRepository : R2dbcRepository<Client, Int> {
}