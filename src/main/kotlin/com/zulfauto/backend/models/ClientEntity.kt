package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "client", schema = "public", catalog = "postgres")
open class ClientEntity {
    @get:Basic
    @get:Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Int? = null

    @get:Id
    @get:Column(name = "id_no", nullable = false)
    var idNo: String? = null

    @get:Basic
    @get:Column(name = "first_name", nullable = true)
    var firstName: String? = null

    @get:Basic
    @get:Column(name = "last_name", nullable = true)
    var lastName: String? = null

    @get:Basic
    @get:Column(name = "email", nullable = true)
    var email: String? = null

    @get:Basic
    @get:Column(name = "post_code", nullable = true)
    var postCode: String? = null

    @get:Basic
    @get:Column(name = "address", nullable = true)
    var address: String? = null

    @get:Basic
    @get:Column(name = "phone_no", nullable = true)
    var phoneNo: String? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "id", referencedColumnName = "client")
    var refClientPaymentEntity: ClientPaymentEntity? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "id", referencedColumnName = "client")
    var refClientUnpaidPaymentEntity: ClientUnpaidPaymentEntity? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "idNo = $idNo " +
                "firstName = $firstName " +
                "lastName = $lastName " +
                "email = $email " +
                "postCode = $postCode " +
                "address = $address " +
                "phoneNo = $phoneNo " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ClientEntity

        if (id != other.id) return false
        if (idNo != other.idNo) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (email != other.email) return false
        if (postCode != other.postCode) return false
        if (address != other.address) return false
        if (phoneNo != other.phoneNo) return false

        return true
    }

}

