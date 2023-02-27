package com.zulfauto.backend.repositories

import com.zulfauto.backend.models.ClientPayment
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface ClientPaymentRepository : R2dbcRepository<ClientPayment, Int> {
}