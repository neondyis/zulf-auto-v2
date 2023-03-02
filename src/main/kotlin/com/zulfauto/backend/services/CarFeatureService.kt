package com.zulfauto.backend.services

import com.zulfauto.backend.dtos.CarFeatureDto
import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.CarFeature
import com.zulfauto.backend.models.Feature
import com.zulfauto.backend.repositories.CarFeatureRepository
import com.zulfauto.backend.repositories.CarRepository
import com.zulfauto.backend.repositories.FeatureRepository
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
import reactor.util.function.Tuple2

@Service
class CarFeatureService(
    @Autowired private val carFeatureRepository: CarFeatureRepository,
    @Autowired private val carRepository: CarRepository,
    @Autowired private val featureRepository: FeatureRepository
) {
    fun getAll(): Flux<CarFeatureDto> {
        return carFeatureRepository.findAll()
            .flatMap { cf ->
                Mono.just(cf)
                    .zipWith(carRepository.findById(cf.car!!).zipWith(featureRepository.findById(cf.feature!!)))
            }
            .map { cf -> mapToDTO(cf) }
            .switchIfEmpty(Mono.just(CarFeatureDto()))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     * Commented out code is if there is no match return everything but dont think that is needed
     **/
    fun getAllByDynamicFilter(carFeature: CarFeature): Flux<CarFeatureDto> {
        return carFeatureRepository.findAll(Example.of(carFeature, ExampleMatcher.matchingAll().withIgnoreCase()))
            .switchIfEmpty(carFeatureRepository.findAll(Example.of(carFeature, ExampleMatcher.matchingAny())))
            .doOnError { error -> log.error("Failed to get car features in dynamic query.", error) }
            .flatMap { cf ->
                Mono.just(cf)
                    .zipWith(carRepository.findById(cf.car!!).zipWith(featureRepository.findById(cf.feature!!)))
            }
            .map { cf -> mapToDTO(cf) }
//            .switchIfEmpty(carRepository.findAll(Example.of(car, ExampleMatcher.matching().withIncludeNullValues())))
    }

    fun update(carFeature: CarFeature): Mono<CarFeatureDto> {
        return if (carFeature.id !== null) {
            updateHelper(carFeature)
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a car feature id"))
    }

    fun bulkUpdate(carFeatures: Flux<CarFeature>): Mono<MutableList<CarFeatureDto>> {
        return carFeatures
            .publishOn(Schedulers.boundedElastic())
            .flatMap { cf -> updateHelper(cf) }
            .collectList()
    }

    fun updateHelper(carFeature: CarFeature): Mono<CarFeatureDto> {
        return carFeatureRepository.findById(carFeature.id!!)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No Car Feature found with Id: " + carFeature.id
                    )
                )
            )
            .publishOn(Schedulers.boundedElastic())
            .flatMap { dest ->
                val cf = updateCarFeature(carFeature, dest)
                carFeatureRepository.save(cf)
            }.flatMap { cf ->
                Mono.just(cf)
                    .zipWith(carRepository.findById(cf.car!!).zipWith(featureRepository.findById(cf.feature!!)))
            }
            .map { cf -> mapToDTO(cf) }
    }


    fun delete(id: Int): Mono<Void> {
        return carFeatureRepository.findById(id)
            .flatMap { carFeatureRepository.deleteById(id) }
            .doOnError { error -> log.error("Failed to delete car feature.", error) }
            .doOnSuccess { log.info("Deleted car feature successfully $id") }
            .then()
    }

    @jakarta.transaction.Transactional
    fun save(carFeature: CarFeature): Mono<CarFeature> {
        return carFeatureRepository.save(carFeature)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Error creating car feature")))
            .doOnError { error -> log.error("Failed to save car feature.", error) }
            .doOnSuccess { c -> log.info(c.toString()) }
    }

    fun import(carFeatures: Flux<CarFeature>): Mono<MutableList<CarFeature>> {
        return carFeatures
            .publishOn(Schedulers.boundedElastic())
            .filter { it.car != null }
            .flatMap { cf -> carFeatureRepository.save(cf) }
            .collectList()
    }
}

private fun updateCarFeature(src: CarFeature, dest: CarFeature): CarFeature {
    dest.car = src.car
    dest.feature = src.feature
    return dest
}

private fun mapToDTO(carFeatureTuple: Tuple2<CarFeature, Tuple2<Car, Feature>>): CarFeatureDto {
    return CarFeatureDto(carFeatureTuple.t1, carFeatureTuple.t2.t2, carFeatureTuple.t2.t1)
}