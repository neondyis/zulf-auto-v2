package com.zulfauto.backend.services

import com.zulfauto.backend.models.Expense
import com.zulfauto.backend.repositories.ExpenseRepository
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
class ExpenseService (@Autowired private val expenseRepository: ExpenseRepository) {
    fun getAll (): Flux<Expense> {
        return expenseRepository.findAll()
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Expense found")))
    }

    /**
     * Checks the database for anything matching in AND condition of nonnull values
     * Then if empty checks the database in OR condition of nonnull values
     **/
    fun getAllByDynamicFilter (expense: Expense): Flux<Expense> {
        return expenseRepository.findAll(Example.of(expense, ExampleMatcher.matchingAll().withIgnoreCase()), Sort.unsorted())
            .switchIfEmpty(expenseRepository.findAll(Example.of(expense, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to get Expense in dynamic query.", error)  }
    }

    fun update (expense: Expense): Mono<Expense> {
        return if (expense.id !== null) {
            expenseRepository.findById(expense.id!!)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Expense found with Id: " + expense.id)))
                .publishOn(Schedulers.boundedElastic())
                .doOnSuccess { dest ->
                    val e = updateExpensePaidPayment(expense, dest)
                    expenseRepository.save(e)
                        .subscribe()
                }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a Expense id"))
    }

    fun delete (id: Int): Mono<Void> {
        return expenseRepository.findById(id)
            .flatMap { expenseRepository.deleteById(id) }
            .doOnError { error -> SqmNode.log.error("Failed to delete Expense.", error)  }
            .doOnSuccess { SqmNode.log.info("Deleted Client Unpaid Payment successfully $id") }
            .then()
    }

    fun save(expense: Expense): Mono<Expense> {
        return expenseRepository.save(expense)
            .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"Error creating Expense")) )
            .doOnError { error -> SqmNode.log.error("Failed to save Expense.", error)  }
            .doOnSuccess { c -> SqmNode.log.info(c.toString()) }

    }
}

private fun updateExpensePaidPayment(src: Expense, dest: Expense): Expense {
    dest.car = src.car
    dest.fuel = src.fuel
    dest.documentation = src.documentation
    dest.importTax = src.importTax
    dest.trailer = src.trailer
    dest.travelCost = src.travelCost
    dest.shipping = src.shipping
    return dest
}
