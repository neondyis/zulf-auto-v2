package com.zulfauto.backend.services

import com.zulfauto.backend.models.OtherExpense
import com.zulfauto.backend.repositories.OtherExpenseRepository
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
class OtherExpenseService (@Autowired private val otherExpenseRepository: OtherExpenseRepository) {
    fun getAll (): Flux<OtherExpense> {
        return otherExpenseRepository.findAll()
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Expense found")))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter (expense: OtherExpense): Flux<OtherExpense> {
        return otherExpenseRepository.findAll(Example.of(expense, ExampleMatcher.matchingAll().withIgnoreCase()), Sort.unsorted())
            .switchIfEmpty(otherExpenseRepository.findAll(Example.of(expense, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to get Expense in dynamic query.", error)  }
    }

    fun update (expense: OtherExpense): Mono<OtherExpense> {
        return if (expense.id !== null) {
            otherExpenseRepository.findById(expense.id!!)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Expense found with Id: " + expense.id)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess { dest ->
                    val e = updateExpensePaidPayment(expense, dest)
                    otherExpenseRepository.save(e)
                        .subscribe()
                }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a Expense id"))
    }

    fun delete (id: Int): Mono<Void> {
        return otherExpenseRepository.findById(id)
            .flatMap { otherExpenseRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Expense.", error)  }
            .doOnSuccess { SqmNode.log.info("Deleted Client Unpaid Payment successfully $id") }
            .then()
    }

    fun save(expense: OtherExpense): Mono<OtherExpense> {
        return otherExpenseRepository.save(expense)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating Expense")) )
            .doOnError { error -> SqmNode.log.error("Failed to save Expense.", error)  }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }
}

private fun updateExpensePaidPayment(src: OtherExpense, dest: OtherExpense): OtherExpense {
    dest.car = src.car
    dest.name = src.name
    dest.cost = src.cost
    return dest
}
