package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "client_payment", schema = "public", catalog = "postgres")
open class ClientPaymentEntity {
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
    @get:Column(name = "interval", nullable = false)
    var interval: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "start_date", nullable = false)
    var startDate: java.sql.Date? = null

    @get:Basic
    @get:Column(name = "car", nullable = false, insertable = false, updatable = false)
    var car: Int? = null

    @get:OneToMany(mappedBy = "refClientPaymentEntity")
    var refClientEntities: List<ClientEntity>? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "car", referencedColumnName = "id")
    var refCarEntity: CarEntity? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "client = $client " +
                "amount = $amount " +
                "interval = $interval " +
                "startDate = $startDate " +
                "car = $car " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ClientPaymentEntity

        if (id != other.id) return false
        if (client != other.client) return false
        if (amount != other.amount) return false
        if (interval != other.interval) return false
        if (startDate != other.startDate) return false
        if (car != other.car) return false

        return true
    }

}

