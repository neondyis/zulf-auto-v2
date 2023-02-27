package com.zulfauto.backend.services

import com.zulfauto.backend.models.ClientPayment
import com.zulfauto.backend.repositories.ClientPaymentRepository
import org.hibernate.query.sqm.tree.SqmNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

@Service
class ClientPaymentService (@Autowired
private val clientPaymentRepository: ClientPaymentRepository) {
    fun getAll (): Flux<ClientPayment> {
        return clientPaymentRepository.findAll()
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Client Payments found")))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter (clientPayment: ClientPayment): Flux<ClientPayment> {
        return clientPaymentRepository.findAll(Example.of(clientPayment, ExampleMatcher.matchingAll().withIgnoreCase()), Sort.unsorted())
            .switchIfEmpty(clientPaymentRepository.findAll(Example.of(clientPayment, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to get Client Payments in dynamic query.", error)  }
    }

    fun update (clientPayment: ClientPayment): Mono<ClientPayment> {
        return if (clientPayment.id !== null) {
            clientPaymentRepository.findById(clientPayment.id!!)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Client Payment found with Id: " + clientPayment.id)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess { dest ->
                    val cp = updateClientPayment(clientPayment, dest)
                    clientPaymentRepository.save(cp)
                        .subscribe()
                }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a Client Payment feature id"))
    }

    fun delete (id: Int): Mono<Void> {
        return clientPaymentRepository.findById(id)
            .flatMap { clientPaymentRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Client Payment.", error)  }
            .doOnSuccess { SqmNode.log.info("Deleted Client Payment successfully $id") }
            .then()
    }

    fun save(clientPayment: ClientPayment): Mono<ClientPayment> {
        return clientPaymentRepository.save(clientPayment)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating Client Payment")) )
            .doOnError { error -> SqmNode.log.error("Failed to save Client Payment.", error)  }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }
}

private fun updateClientPayment(src: ClientPayment, dest: ClientPayment): ClientPayment {
    dest.client = src.client
    dest.car = src.car
    dest.amount = src.amount
    dest.initialPayment = src.initialPayment
    dest.interval = src.interval
    dest.startDate = src.startDate
    return dest
}