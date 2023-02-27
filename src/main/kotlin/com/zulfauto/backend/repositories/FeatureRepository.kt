package com.zulfauto.backend.repositories

import com.zulfauto.backend.models.Feature
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Flux

interface FeatureRepository : R2dbcRepository<Feature, Int> {
    fun findAllByName(name:String?): Flux<Feature>
}