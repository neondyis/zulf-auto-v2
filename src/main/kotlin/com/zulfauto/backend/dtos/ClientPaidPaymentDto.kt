package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.ClientPaidPayment
import com.zulfauto.backend.models.ClientPayment
import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.ClientPaidPayment} entity
 */
data class ClientPaidPaymentDto(
    val clientPaidPayment: ClientPaidPayment? = null,
    @field:NotNull val clientPayment: ClientPayment? = null
) : Serializable