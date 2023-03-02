package com.zulfauto.backend.services

import com.zulfauto.backend.dtos.ClientPaidPaymentDto
import com.zulfauto.backend.models.ClientPaidPayment
import com.zulfauto.backend.models.ClientPayment
import com.zulfauto.backend.repositories.ClientPaidPaymentRepository
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
import reactor.util.function.Tuple2

@Service
class ClientPaidPaymentService(
    @Autowired private val clientPaidPaymentRepository: ClientPaidPaymentRepository,
    @Autowired private val clientPaymentRepository: ClientPaymentRepository
) {
    fun getAll(): Flux<ClientPaidPaymentDto> {
        return clientPaidPaymentRepository.findAll()
            .flatMap { cpp -> Mono.just(cpp).zipWith(clientPaymentRepository.findById(cpp.clientPayment!!)) }
            .map { cpp -> mapToDTO(cpp) }
            .switchIfEmpty(Mono.just(ClientPaidPaymentDto()))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter(clientPaidPayment: ClientPaidPayment): Flux<ClientPaidPaymentDto> {
        return clientPaidPaymentRepository.findAll(
            Example.of(
                clientPaidPayment,
                ExampleMatcher.matchingAll().withIgnoreCase()
            ), Sort.unsorted()
        )
            .switchIfEmpty(
                clientPaidPaymentRepository.findAll(
                    Example.of(
                        clientPaidPayment,
                        ExampleMatcher.matchingAny()
                    )
                )
            )
            .doOnError { error -> SqmNode.log.error("Failed to get Client Paid Payment in dynamic query.", error) }
            .flatMap { cpp -> Mono.just(cpp).zipWith(clientPaymentRepository.findById(cpp.clientPayment!!)) }
            .map { cpp -> mapToDTO(cpp) }
    }

    fun update(clientPaidPayment: ClientPaidPayment): Mono<ClientPaidPaymentDto> {
        return if (clientPaidPayment.id !== null) {
            updateHelper(clientPaidPayment)
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST, "Provide a Client Paid Payment id"))
    }

    fun bulkUpdate(clientPaidPayments: Flux<ClientPaidPayment>): Mono<MutableList<ClientPaidPaymentDto>> {
        return clientPaidPayments
            .publishOn(Schedulers.boundedElastic())
            .flatMap { cf -> updateHelper(cf) }
            .collectList()
    }

    fun updateHelper(clientPaidPayment: ClientPaidPayment): Mono<ClientPaidPaymentDto> {
        return clientPaidPaymentRepository.findById(clientPaidPayment.id!!)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No Client Paid Payment found with Id: " + clientPaidPayment.id
                    )
                )
            )
            .publishOn(Schedulers.boundedElastic())
            .flatMap { dest ->
                val cpp = updateClientPaidPayment(clientPaidPayment, dest)
                clientPaidPaymentRepository.save(cpp)
            }.flatMap { cpp -> Mono.just(cpp).zipWith(clientPaymentRepository.findById(cpp.clientPayment!!)) }
            .map { cpp -> mapToDTO(cpp) }
    }

    fun delete(id: Int): Mono<Void> {
        return clientPaidPaymentRepository.findById(id)
            .flatMap { clientPaidPaymentRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Client Paid Payment.", error) }
            .doOnSuccess { SqmNode.log.info("Deleted Client Paid Payment successfully $id") }
            .then()
    }

    fun save(clientPaidPayment: ClientPaidPayment): Mono<ClientPaidPayment> {
        return clientPaidPaymentRepository.save(clientPaidPayment)
            .switchIfEmpty(
                Mono.error(
                    ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Error creating Client Paid Payment"
                    )
                )
            )
            .doOnError { error -> SqmNode.log.error("Failed to save Client Paid Payment.", error) }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }

    fun import(clientPaidPayments: Flux<ClientPaidPayment>): Mono<MutableList<ClientPaidPayment>> {
        return clientPaidPayments
            .publishOn(Schedulers.boundedElastic())
            .filter { it.amount != null }
            .flatMap { cpp -> clientPaidPaymentRepository.save(cpp) }
            .collectList()
    }
}

private fun updateClientPaidPayment(src: ClientPaidPayment, dest: ClientPaidPayment): ClientPaidPayment {
    dest.clientPayment = src.clientPayment
    dest.amount = src.amount
    dest.date = src.date
    return dest
}

private fun mapToDTO(clientPaymentTuple: Tuple2<ClientPaidPayment, ClientPayment>): ClientPaidPaymentDto {
    return ClientPaidPaymentDto(clientPaymentTuple.t1, clientPaymentTuple.t2)
}