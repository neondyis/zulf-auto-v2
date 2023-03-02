package com.zulfauto.backend.dtos

import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.Expense
import java.io.Serializable

/**
 * A DTO for the {@link com.zulfauto.backend.models.Expense} entity
 */
data class ExpenseDto(
    val expense: Expense? = null,
    val car: Car? = null,
) : Serializable