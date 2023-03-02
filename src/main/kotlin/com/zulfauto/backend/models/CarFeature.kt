package com.zulfauto.backend.models

import jakarta.persistence.Column
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Table
import jakarta.validation.constraints.NotNull
import org.springframework.data.annotation.Id

@Table(name = "car_feature")
open class CarFeature {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "feature", nullable = false)
    open var feature: Int? = null

    @NotNull
    @Column(name = "car", nullable = false)
    open var car: Int? = null

    @Column(name = "value", length = Integer.MAX_VALUE)
    open var value: String? = null
}