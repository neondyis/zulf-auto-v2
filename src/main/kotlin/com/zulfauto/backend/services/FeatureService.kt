package com.zulfauto.backend.services

import com.zulfauto.backend.models.Feature
import com.zulfauto.backend.repositories.FeatureRepository
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class FeatureService(@Autowired private val featureRepository: FeatureRepository) {
    fun getAll (): Flux<Feature> {
        return featureRepository.findAll()
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Features found")))
    }

    fun getAllByDynamicFilter (name: String): Flux<Feature> {
        return featureRepository.findAllByName(name)
            .doOnError { error -> log.error("Failed to get features in query.", error)  }
    }

    fun update (feature: Feature): Mono<Feature> {
        return if (feature.id !== null) {
            featureRepository.findById(feature.id!!)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Feature found with Id: " + feature.id)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess { dest ->
                    val car2 = updateFeature(feature, dest)
                    featureRepository.save(car2)
                        .subscribe()
                }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a feature id"))
    }

    fun delete (id: Int): Mono<Void> {
        return featureRepository.findById(id)
            .flatMap { featureRepository.deleteById(id) }
            .doOnError { error -> log.error("Failed to delete feature.", error)  }
            .doOnSuccess { log.info("Deleted feature successfully $id") }
            .then()
    }

    fun save(carFeature: Feature): Mono<Feature> {
        return featureRepository.save(carFeature)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating car feature")) )
            .doOnError { error -> log.error("Failed to save car feature.", error)  }
            .doOnSuccess { c -> log.info(c.toString()) }

    }
}

private fun updateFeature(src: Feature, dest: Feature): Feature {
    dest.name = src.name
    return dest
}