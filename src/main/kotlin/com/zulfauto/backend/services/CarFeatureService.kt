package com.zulfauto.backend.services

import com.zulfauto.backend.models.CarFeature
import com.zulfauto.backend.repositories.CarFeatureRepository
import org.hibernate.query.sqm.tree.SqmNode.log
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
class CarFeatureService(@Autowired private val carFeatureRepository: CarFeatureRepository) {
    fun getAll (): Flux<CarFeature> {
        return carFeatureRepository.findAll()
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Car Features found")))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     * Commented out code is if there is no match return everything but dont think that is needed
     **/
    fun getAllByDynamicFilter (carFeature: CarFeature): Flux<CarFeature> {
        return carFeatureRepository.findAll(Example.of(carFeature, ExampleMatcher.matchingAll().withIgnoreCase()))
            .switchIfEmpty(carFeatureRepository.findAll(Example.of(carFeature, ExampleMatcher.matchingAny())))
            .doOnError { error -> log.error("Failed to get car features in dynamic query.", error)  }
//            .switchIfEmpty(carRepository.findAll(Example.of(car, ExampleMatcher.matching().withIncludeNullValues())))
    }

    fun update (car: CarFeature): Mono<CarFeature> {
        return if (car.id !== null) {
            carFeatureRepository.findById(car.id!!)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Car found with Id: " + car.id)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess { dest ->
                    val carFeature = updateCarFeature(car, dest)
                    carFeatureRepository.save(carFeature)
                        .subscribe()
                }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a car feature id"))
    }

    fun delete (id: Int): Mono<Void> {
        return carFeatureRepository.findById(id)
            .flatMap { carFeatureRepository.deleteById(id) }
            .doOnError { error -> log.error("Failed to delete car feature.", error)  }
            .doOnSuccess { log.info("Deleted car feature successfully $id") }
            .then()
    }

    @jakarta.transaction.Transactional
    fun save(carFeature: CarFeature): Mono<CarFeature> {
        return carFeatureRepository.save(carFeature)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating car feature")) )
            .doOnError { error -> log.error("Failed to save car feature.", error)  }
            .doOnSuccess { c -> log.info(c.toString()) }

    }
}

private fun updateCarFeature(src: CarFeature, dest: CarFeature): CarFeature {
    dest.car = src.car
    dest.feature = src.feature
    return dest
}