package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.ClientPayment
import com.zulfauto.backend.models.ClientUnpaidPayment
import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.ClientUnpaidPayment} entity
 */
data class ClientUnpaidPaymentDto(
    val clientUnPaidPayment: ClientUnpaidPayment? = null,
    @field:NotNull val clientPayment: ClientPayment? = null,
) : Serializable