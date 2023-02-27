package com.zulfauto.backend.models

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import org.springframework.data.annotation.Id
import java.math.BigDecimal

@Table(name = "car")
open class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Generated(GenerationTime.INSERT)
    @Column(name = "id", nullable = false,columnDefinition = "serial", updatable = false)
    open var id: Int? = null

    @NotNull
    @Column(name = "model", nullable = false, length = Integer.MAX_VALUE)
    open var model: String? = null

    @NotNull
    @Column(name = "brand", nullable = false, length = Integer.MAX_VALUE)
    @field:NotBlank
    open var brand: String? = null

    @Column(name = "door_no")
    open var doorNo: Int? = null

    @Column(name = "registration")
    open var registration: Int? = null

    @Column(name = "kilometers")
    open var kilometers: BigDecimal? = null

    @Column(name = "purchase_price")
    open var purchasePrice: BigDecimal? = null

    @Column(name = "colour", length = Integer.MAX_VALUE)
    open var colour: String? = null

    @Column(name = "metallic")
    open var metallic: Boolean? = null

    @Column(name = "region", length = Integer.MAX_VALUE)
    open var region: String? = null

    @Column(name = "emission_class", length = Integer.MAX_VALUE)
    open var emissionClass: String? = null

    @Column(name = "horsepower")
    open var horsepower: BigDecimal? = null

    @Column(name = "emission_sticker", length = Integer.MAX_VALUE)
    open var emissionSticker: String? = null

    @Column(name = "fuel_type", length = Integer.MAX_VALUE)
    open var fuelType: String? = null

    @Column(name = "cubic_capacity")
    open var cubicCapacity: BigDecimal? = null

    @Column(name = "selling_price")
    open var sellingPrice: BigDecimal? = null

    @Column(name = "last_updated_by")
    open var lastUpdatedBy: Int? = null

    override fun toString(): String {
        return "Car(id=$id, model=$model, brand=$brand, doorNo=$doorNo, registration=$registration, kilometers=$kilometers, purchasePrice=$purchasePrice, colour=$colour, metallic=$metallic, region=$region, emissionClass=$emissionClass, horsepower=$horsepower, emissionSticker=$emissionSticker, fuelType=$fuelType, cubicCapacity=$cubicCapacity, sellingPrice=$sellingPrice, lastUpdatedBy=$lastUpdatedBy)"
    }
}