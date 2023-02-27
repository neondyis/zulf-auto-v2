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

    fun singleUpdate (car: Car): Mono<Car> {
        return if (car.id !== null) {
            updateHelper(car)
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a car id"))
    }

    fun bulkUpdate(cars: Flux<Car>): Mono<MutableList<Car>> {
        return cars
            .publishOn(Schedulers.boundedElastic())
            .flatMap{ car-> updateHelper(car) }
            .collectList()
    }

    fun updateHelper(car: Car): Mono<Car> {
        return carRepository.findById(car.id!!)
            .flatMap { dest ->
                carRepository.save(update(car, dest))
            }
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "No Car found with Id: " + car.id)))
            .publishOn(Schedulers.boundedElastic())
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
    src.model?.let{ dest.model = src.model }
    src.brand?.let{ dest.brand = src.brand }
    src.doorNo?.let{ dest.doorNo = src.doorNo }
    src.registration?.let{ dest.registration = src.registration }
    src.kilometers?.let{ dest.kilometers = src.kilometers }
    src.purchasePrice?.let{dest.purchasePrice = src.purchasePrice}
    src.colour?.let{dest.colour = src.colour}
    src.metallic?.let{dest.metallic = src.metallic}
    src.region?.let{dest.region = src.region}
    src.emissionClass?.let{dest.emissionClass = src.emissionClass}
    src.horsepower?.let{dest.horsepower = src.horsepower}
    src.emissionSticker?.let{dest.emissionSticker = src.emissionSticker}
    src.fuelType?.let{dest.fuelType = src.fuelType}
    src.cubicCapacity?.let{dest.cubicCapacity = src.cubicCapacity}
    src.sellingPrice?.let{dest.sellingPrice = src.sellingPrice}
    src.lastUpdatedBy?.let{dest.lastUpdatedBy = src.lastUpdatedBy}
    return dest
}