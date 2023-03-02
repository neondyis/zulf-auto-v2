package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.Client
import com.zulfauto.backend.models.ClientPayment
import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.ClientPayment} entity
 */
data class ClientPaymentDto(
    val clientPayment: ClientPayment? = null,
    @field:NotNull val car: Car? = null,
    @field:NotNull val client: Client? = null,
) : Serializable