package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "client_unpaid_payment", schema = "public", catalog = "postgres")
open class ClientUnpaidPaymentEntity {
    @get:Id
    @get:Column(name = "id", nullable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "client", nullable = false, insertable = false, updatable = false)
    var client: Int? = null

    @get:Basic
    @get:Column(name = "amount", nullable = false)
    var amount: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "date", nullable = false)
    var date: java.sql.Date? = null

    @get:Basic
    @get:Column(name = "paid", nullable = false)
    var paid: java.lang.Boolean? = null

    @get:OneToMany(mappedBy = "refClientUnpaidPaymentEntity")
    var refClientEntities: List<ClientEntity>? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "client = $client " +
                "amount = $amount " +
                "date = $date " +
                "paid = $paid " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ClientUnpaidPaymentEntity

        if (id != other.id) return false
        if (client != other.client) return false
        if (amount != other.amount) return false
        if (date != other.date) return false
        if (paid != other.paid) return false

        return true
    }

}

