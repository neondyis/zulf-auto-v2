package com.zulfauto.backend.repositories

import com.zulfauto.backend.models.Car
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface CarRepository : R2dbcRepository<Car, Int> {
}