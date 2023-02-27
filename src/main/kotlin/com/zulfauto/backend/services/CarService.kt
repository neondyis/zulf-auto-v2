package com.zulfauto.backend.services

import com.zulfauto.backend.models.Car
import com.zulfauto.backend.repositories.CarRepository
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class CarService (@Autowired private val carRepository: CarRepository) {
    private val log = LoggerFactory.getLogger(CarService::class.java)
    fun getAll (): Flux<Car> {
        return carRepository.findAll()
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Cars found")))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     * Commented out code is if there is no match return everything but dont think that is needed
     **/
    fun getAllByDynamicFilter (car: Car): Flux<Car> {
        log.info(car.toString())
        return carRepository.findAll(Example.of(car, ExampleMatcher.matchingAll().withIgnoreCase()))
            .switchIfEmpty(carRepository.findAll(Example.of(car, ExampleMatcher.matchingAny().withIgnoreCase())))
            .doOnError { error -> log.error("Failed to get cars in dynamic query.", error)  }
//            .switchIfEmpty(carRepository.findAll(Example.of(car, ExampleMatcher.matching().withIncludeNullValues())))
    }

    fun update (car: Car): Mono<Car> {
        return if (car.id !== null) {
            carRepository.findById(car.id!!)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Car found with Id: " + car.id)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess { dest ->
                    val car2 = update(car, dest)
                    carRepository.save(car2)
                        .subscribe()
                }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a car id"))
    }

    fun delete (id: Int): Mono<Void> {
        return carRepository.findById(id)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Car found id")) )
            .flatMap { carRepository.deleteById(id) }
            .doOnError { error -> log.error("Failed to delete car.", error)  }
            .doOnSuccess { log.info("Deleted car successfully $id") }
            .then()
    }

    fun save(car:Car): Mono<Car> {
        return carRepository.save(car)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating car")) )
            .doOnError { error -> log.error("Failed to save car.", error)  }
            .doOnSuccess { c -> log.info(c.toString()) }

    }
}

private fun update(src: Car, dest: Car): Car {
    dest.model = src.model
    dest.brand = src.brand
    dest.doorNo = src.doorNo
    dest.registration = src.registration
    dest.kilometers = src.kilometers
    dest.purchasePrice = src.purchasePrice
    dest.colour = src.colour
    dest.metallic = src.metallic
    dest.region = src.region
    dest.emissionClass = src.emissionClass
    dest.horsepower = src.horsepower
    dest.emissionSticker = src.emissionSticker
    dest.fuelType = src.fuelType
    dest.cubicCapacity = src.cubicCapacity
    dest.sellingPrice = src.sellingPrice
    dest.lastUpdatedBy = src.lastUpdatedBy
    return dest
}