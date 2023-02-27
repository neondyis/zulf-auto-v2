package com.zulfauto.backend.services

import com.zulfauto.backend.models.Client
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

@Service
class ClientService(@Autowired private val clientRepository: ClientRepository) {
    fun getAll (): Flux<Client> {
        return clientRepository.findAll()
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Client found")))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter (client: Client): Flux<Client> {
        return clientRepository.findAll(Example.of(client, ExampleMatcher.matchingAll().withIgnoreCase()), Sort.unsorted())
            .switchIfEmpty(clientRepository.findAll(Example.of(client, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to find any Clients in dynamic query.", error)  }
    }

    fun update (client: Client): Mono<Client> {
        return if (client.id !== null) {
            clientRepository.findById(client.id!!)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Client found with Id: " + client.id)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess { dest ->
                    val carFeature = updateClient(client, dest)
                    clientRepository.save(carFeature)
                        .subscribe()
                }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a client feature id"))
    }

    fun delete (id: Int): Mono<Void> {
        return clientRepository.findById(id)
            .flatMap { clientRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Client.", error)  }
            .doOnSuccess { SqmNode.log.info("Deleted Client successfully $id") }
            .then()
    }

    fun save(client: Client): Mono<Client> {
        return clientRepository.save(client)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating Client")) )
            .doOnError { error -> SqmNode.log.error("Failed to save Client.", error)  }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }
}

private fun updateClient(src: Client, dest: Client): Client {
    dest.email = src.email
    dest.firstName = src.firstName
    dest.address = src.address
    dest.documentId = src.documentId
    dest.lastName = src.lastName
    dest.phoneNo = src.phoneNo
    dest.postCode = src.postCode
    return dest
}