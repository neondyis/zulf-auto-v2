package com.zulfauto.backend.repositories;

import com.zulfauto.backend.models.Users
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface UserRepository : R2dbcRepository<Users, Int> {
    fun findUserByUsername(username: String?): Mono<Users>;
}