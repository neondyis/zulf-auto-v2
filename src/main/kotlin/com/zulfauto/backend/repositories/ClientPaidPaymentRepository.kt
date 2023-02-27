package com.zulfauto.backend.repositories

import com.zulfauto.backend.models.ClientPaidPayment
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface ClientPaidPaymentRepository : R2dbcRepository<ClientPaidPayment, Int> {
}