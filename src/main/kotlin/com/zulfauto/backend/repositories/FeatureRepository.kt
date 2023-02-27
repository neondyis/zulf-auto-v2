package com.zulfauto.backend.repositories;

import com.zulfauto.backend.models.Feature
import org.springframework.data.r2dbc.repository.R2dbcRepository
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

interface FeatureRepository : R2dbcRepository<Feature, Int> {
    fun findAllByName(name:String?): Flux<Feature>
}