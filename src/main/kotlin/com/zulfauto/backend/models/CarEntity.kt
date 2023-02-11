package com.zulfauto.backend.models

import jakarta.persistence.*

@Entity
@Table(name = "car", schema = "public", catalog = "postgres")
open class CarEntity {
    @get:Id
    @get:Column(name = "id", nullable = false, insertable = false, updatable = false)
    var id: Int? = null

    @get:Basic
    @get:Column(name = "model", nullable = false)
    var model: String? = null

    @get:Basic
    @get:Column(name = "brand", nullable = false)
    var brand: String? = null

    @get:Basic
    @get:Column(name = "door_no", nullable = true)
    var doorNo: Int? = null

    @get:Basic
    @get:Column(name = "registration", nullable = true)
    var registration: Int? = null

    @get:Basic
    @get:Column(name = "kilometers", nullable = true)
    var kilometers: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "purchase_price", nullable = true)
    var purchasePrice: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "colour", nullable = true)
    var colour: String? = null

    @get:Basic
    @get:Column(name = "metallic", nullable = true)
    var metallic: java.lang.Boolean? = null

    @get:Basic
    @get:Column(name = "region", nullable = true)
    var region: String? = null

    @get:Basic
    @get:Column(name = "emission_class", nullable = true)
    var emissionClass: String? = null

    @get:Basic
    @get:Column(name = "horsepower", nullable = true)
    var horsepower: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "emission_sticker", nullable = true)
    var emissionSticker: String? = null

    @get:Basic
    @get:Column(name = "fuel_type", nullable = true)
    var fuelType: String? = null

    @get:Basic
    @get:Column(name = "cubic_capacity", nullable = true)
    var cubicCapacity: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "selling_price", nullable = true)
    var sellingPrice: java.math.BigInteger? = null

    @get:Basic
    @get:Column(name = "last_updated_by", nullable = true, insertable = false, updatable = false)
    var lastUpdatedBy: Int? = null

    @get:ManyToOne(fetch = FetchType.LAZY)
    @get:JoinColumn(name = "last_updated_by", referencedColumnName = "id")
    var refUserEntity: UserEntity? = null

    @get:OneToMany(mappedBy = "refCarEntity")
    var refCarFeatureEntities: List<CarFeatureEntity>? = null

    @get:OneToMany(mappedBy = "refCarEntity")
    var refClientPaymentEntities: List<ClientPaymentEntity>? = null

    @get:OneToMany(mappedBy = "refCarEntity")
    var refExpenseEntities: List<ExpenseEntity>? = null

    @get:OneToMany(mappedBy = "refCarEntity")
    var refOtherExpenseEntities: List<OtherExpenseEntity>? = null

    override fun toString(): String =
        "Entity of type: ${javaClass.name} ( " +
                "id = $id " +
                "model = $model " +
                "brand = $brand " +
                "doorNo = $doorNo " +
                "registration = $registration " +
                "kilometers = $kilometers " +
                "purchasePrice = $purchasePrice " +
                "colour = $colour " +
                "metallic = $metallic " +
                "region = $region " +
                "emissionClass = $emissionClass " +
                "horsepower = $horsepower " +
                "emissionSticker = $emissionSticker " +
                "fuelType = $fuelType " +
                "cubicCapacity = $cubicCapacity " +
                "sellingPrice = $sellingPrice " +
                "lastUpdatedBy = $lastUpdatedBy " +
                ")"

    // constant value returned to avoid entity inequality to itself before and after it's update/merge
    override fun hashCode(): Int = 42

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as CarEntity

        if (id != other.id) return false
        if (model != other.model) return false
        if (brand != other.brand) return false
        if (doorNo != other.doorNo) return false
        if (registration != other.registration) return false
        if (kilometers != other.kilometers) return false
        if (purchasePrice != other.purchasePrice) return false
        if (colour != other.colour) return false
        if (metallic != other.metallic) return false
        if (region != other.region) return false
        if (emissionClass != other.emissionClass) return false
        if (horsepower != other.horsepower) return false
        if (emissionSticker != other.emissionSticker) return false
        if (fuelType != other.fuelType) return false
        if (cubicCapacity != other.cubicCapacity) return false
        if (sellingPrice != other.sellingPrice) return false
        if (lastUpdatedBy != other.lastUpdatedBy) return false

        return true
    }

}

