package com.zulfauto.backend.models

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id

@Table(name = "client")
open class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "id_no", nullable = false, length = Integer.MAX_VALUE)
    open var documentId: String? = null

    @Column(name = "first_name", length = Integer.MAX_VALUE)
    open var firstName: String? = null

    @Column(name = "last_name", length = Integer.MAX_VALUE)
    open var lastName: String? = null

    @Column(name = "email", length = Integer.MAX_VALUE)
    open var email: String? = null

    @Column(name = "post_code", length = Integer.MAX_VALUE)
    open var postCode: String? = null

    @Column(name = "address", length = Integer.MAX_VALUE)
    open var address: String? = null

    @Column(name = "phone_no", length = Integer.MAX_VALUE)
    open var phoneNo: String? = null
}