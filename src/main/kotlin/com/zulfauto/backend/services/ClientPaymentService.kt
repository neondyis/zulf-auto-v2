package com.zulfauto.backend.services

import com.zulfauto.backend.dtos.ClientPaymentDto
import com.zulfauto.backend.models.Car
import com.zulfauto.backend.models.Client
import com.zulfauto.backend.models.ClientPayment
import com.zulfauto.backend.repositories.CarRepository
import com.zulfauto.backend.repositories.ClientPaymentRepository
import com.zulfauto.backend.repositories.ClientRepository
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
import reactor.util.function.Tuple2

@Service
class ClientPaymentService(
    @Autowired private val clientPaymentRepository: ClientPaymentRepository,
    @Autowired private val carRepository: CarRepository,
    @Autowired private val clientRepository: ClientRepository
) {
    fun getAll(): Flux<ClientPaymentDto> {
        return clientPaymentRepository.findAll()
            .flatMap { cp ->
                Mono.just(cp).zipWith(carRepository.findById(cp.car!!).zipWith(clientRepository.findById(cp.client!!)))
            }
            .map { cp -> mapToDTO(cp) }
            .switchIfEmpty(Mono.just(ClientPaymentDto()))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter(clientPayment: ClientPayment): Flux<ClientPaymentDto> {
        return clientPaymentRepository.findAll(
            Example.of(clientPayment, ExampleMatcher.matchingAll().withIgnoreCase()),
            Sort.unsorted()
        )
            .switchIfEmpty(clientPaymentRepository.findAll(Example.of(clientPayment, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to get Client Payments in dynamic query.", error) }
            .flatMap { cp ->
                Mono.just(cp).zipWith(carRepository.findById(cp.car!!).zipWith(clientRepository.findById(cp.client!!)))
            }
            .map { cp -> mapToDTO(cp) }
    }

    fun update(clientPayment: ClientPayment): Mono<ClientPaymentDto> {
        return if (clientPayment.id !== null) {
            updateHelper(clientPayment)
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a Client Payment feature id"))
    }

    fun bulkUpdate(clientPayments: Flux<ClientPayment>): Mono<MutableList<ClientPaymentDto>> {
        return clientPayments
            .publishOn(Schedulers.boundedElastic())
            .flatMap { cf -> updateHelper(cf) }
            .collectList()
    }

    fun updateHelper(clientPayment: ClientPayment): Mono<ClientPaymentDto> {
        return clientPaymentRepository.findById(clientPayment.id!!)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No Client Payment found with Id: " + clientPayment.id
                    )
                )
            )
            .publishOn(Schedulers.boundedElastic())
            .flatMap { dest ->
                val cp = updateClientPayment(clientPayment, dest)
                clientPaymentRepository.save(cp)
            }
            .flatMap { r ->
                Mono.just(r).zipWith(carRepository.findById(r.car!!).zipWith(clientRepository.findById(r.client!!)))
            }
            .map { cp -> mapToDTO(cp) }
    }

    fun delete(id: Int): Mono<Void> {
        return clientPaymentRepository.findById(id)
            .flatMap { clientPaymentRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Client Payment.", error) }
            .doOnSuccess { SqmNode.log.info("Deleted Client Payment successfully $id") }
            .then()
    }

    fun save(clientPayment: ClientPayment): Mono<ClientPayment> {
        return clientPaymentRepository.save(clientPayment)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND, "Error creating Client Payment")))
            .doOnError { error -> SqmNode.log.error("Failed to save Client Payment.", error) }
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

private fun mapToDTO(clientCarTuple: Tuple2<ClientPayment, Tuple2<Car, Client>>): ClientPaymentDto {
    return ClientPaymentDto(clientCarTuple.t1, clientCarTuple.t2.t1, clientCarTuple.t2.t2)
}