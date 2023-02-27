package com.zulfauto.backend.repositories

import com.zulfauto.backend.models.OtherExpense
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface OtherExpenseRepository : R2dbcRepository<OtherExpense, Int> {
}