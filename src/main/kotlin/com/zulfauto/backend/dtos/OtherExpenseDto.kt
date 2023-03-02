package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.OtherExpense
import jakarta.validation.constraints.NotNull
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.OtherExpense} entity
 */
data class OtherExpenseDto(
    val otherExpense: OtherExpense? = null,
    @field:NotNull val car: Car? = null,
) : Serializable