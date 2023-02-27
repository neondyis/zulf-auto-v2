package com.zulfauto.backend.repositories;

import com.zulfauto.backend.models.CarFeature
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface CarFeatureRepository : R2dbcRepository<CarFeature, Int> {
}