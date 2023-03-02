package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.Users
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.Car} entity
 */
data class CarDto(
    val car: Car? = null,
    val lastUpdatedBy: Users? = null
) : Serializable