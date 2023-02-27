package com.zulfauto.backend.repositories

import com.zulfauto.backend.models.ClientUnpaidPayment
import org.springframework.data.r2dbc.repository.R2dbcRepository

interface ClientUnpaidPaymentRepository : R2dbcRepository<ClientUnpaidPayment, Int> {
}