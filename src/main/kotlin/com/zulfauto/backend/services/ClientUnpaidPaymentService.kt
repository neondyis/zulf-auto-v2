package com.zulfauto.backend.services

import com.zulfauto.backend.dtos.ClientUnpaidPaymentDto
import com.zulfauto.backend.models.ClientPayment
import com.zulfauto.backend.models.ClientUnpaidPayment
import com.zulfauto.backend.repositories.ClientPaymentRepository
import com.zulfauto.backend.repositories.ClientUnpaidPaymentRepository
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
class ClientUnpaidPaymentService(
    @Autowired private val clientUnpaidPaymentRepository: ClientUnpaidPaymentRepository,
    @Autowired private val clientPaymentRepository: ClientPaymentRepository
) {
    fun getAll(): Flux<ClientUnpaidPaymentDto> {
        return clientUnpaidPaymentRepository.findAll()
            .flatMap { cpp -> Mono.just(cpp).zipWith(clientPaymentRepository.findById(cpp.clientPayment!!)) }
            .map { cpp -> mapToDTO(cpp) }
            .switchIfEmpty(Mono.just(ClientUnpaidPaymentDto()))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter(clientUnpaidPayment: ClientUnpaidPayment): Flux<ClientUnpaidPaymentDto> {
        return clientUnpaidPaymentRepository.findAll(
            Example.of(
                clientUnpaidPayment,
                ExampleMatcher.matchingAll().withIgnoreCase()
            ), Sort.unsorted()
        )
            .switchIfEmpty(
                clientUnpaidPaymentRepository.findAll(
                    Example.of(
                        clientUnpaidPayment,
                        ExampleMatcher.matchingAny()
                    )
                )
            )
            .doOnError { error -> SqmNode.log.error("Failed to get Client Unpaid Payment in dynamic query.", error) }
            .flatMap { cpp -> Mono.just(cpp).zipWith(clientPaymentRepository.findById(cpp.clientPayment!!)) }
            .map { cpp -> mapToDTO(cpp) }
    }

    fun update(clientUnpaidPayment: ClientUnpaidPayment): Mono<ClientUnpaidPaymentDto> {
        return if (clientUnpaidPayment.id !== null) {
            updateHelper(clientUnpaidPayment)
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a Client Unpaid Payment id"))
    }

    fun bulkUpdate(clientUnpaidPayments: Flux<ClientUnpaidPayment>): Mono<MutableList<ClientUnpaidPaymentDto>> {
        return clientUnpaidPayments
            .publishOn(Schedulers.boundedElastic())
            .flatMap { cf -> updateHelper(cf) }
            .collectList()
    }

    fun updateHelper(clientUnpaidPayment: ClientUnpaidPayment): Mono<ClientUnpaidPaymentDto> {
        return clientUnpaidPaymentRepository.findById(clientUnpaidPayment.id!!)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No Client Unpaid Payment found with Id: " + clientUnpaidPayment.id
                    )
                )
            )
            .publishOn(Schedulers.boundedElastic())
            .flatMap { dest ->
                val cpp = updateClientPaidPayment(clientUnpaidPayment, dest)
                clientUnpaidPaymentRepository.save(cpp)
            }.flatMap { cpp -> Mono.just(cpp).zipWith(clientPaymentRepository.findById(cpp.clientPayment!!)) }
            .map { cpp -> mapToDTO(cpp) }
    }

    fun delete(id: Int): Mono<Void> {
        return clientUnpaidPaymentRepository.findById(id)
            .flatMap { clientUnpaidPaymentRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Client Unpaid Payment.", error) }
            .doOnSuccess { SqmNode.log.info("Deleted Client Unpaid Payment successfully $id") }
            .then()
    }

    fun save(clientUnpaidPayment: ClientUnpaidPayment): Mono<ClientUnpaidPayment> {
        return clientUnpaidPaymentRepository.save(clientUnpaidPayment)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Error creating Client Unpaid Payment"
                    )
                )
            )
            .doOnError { error -> SqmNode.log.error("Failed to save Client Unpaid Payment.", error) }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }

    fun import(clientUnpaidPayments: Flux<ClientUnpaidPayment>): Flux<ClientUnpaidPayment> {
        return clientUnpaidPayments
            .publishOn(Schedulers.boundedElastic())
            .filter { it.clientPayment != null }
            .flatMap { car -> clientUnpaidPaymentRepository.save(car) }
    }
}

private fun updateClientPaidPayment(src: ClientUnpaidPayment, dest: ClientUnpaidPayment): ClientUnpaidPayment {
    dest.clientPayment = src.clientPayment
    dest.amount = src.amount
    dest.date = src.date
    dest.paid = src.paid
    dest.amount_paid = src.amount_paid
    return dest
}

private fun mapToDTO(clientPaymentTuple: Tuple2<ClientUnpaidPayment, ClientPayment>): ClientUnpaidPaymentDto {
    return ClientUnpaidPaymentDto(clientPaymentTuple.t1, clientPaymentTuple.t2)
}
