package com.zulfauto.backend.dtos

import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.Feature} entity
 */
data class FeatureDto(val id: Int? = null, @field:NotNull val name: String? = null) : Serializable