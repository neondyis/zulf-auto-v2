package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.CarFeature
import com.zulfauto.backend.models.Feature
import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.CarFeature} entity
 */
data class CarFeatureDto(
    val carFeature: CarFeature? = null,
    @field:NotNull val feature: Feature? = null,
    @field:NotNull val car: Car? = null,
) : Serializable