package com.zulfauto.backend.repositories;

import com.zulfauto.backend.models.Expense
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface ExpenseRepository : R2dbcRepository<Expense, Int> {
}