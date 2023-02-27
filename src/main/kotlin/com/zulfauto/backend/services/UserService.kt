package com.zulfauto.backend.services

import com.zulfauto.backend.models.Users
import com.zulfauto.backend.repositories.UserRepository
import com.zulfauto.backend.requests.SignupRequest
import org.hibernate.query.sqm.tree.SqmNode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.*
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.*

@Service
class UserService(@Autowired private val userRepository: UserRepository,
                  @Autowired private val encoder: PasswordEncoder,
) {

    fun update(user: Users): Mono<Users> {
        return if (user.id !== null) {
            userRepository.findById(user.id!!)
                    .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No User found with Id: " + user.id)))
                    .publishOn(Schedulers.boundedElastic())
                    .doOnSuccess { user1 ->
                        val user2 = updateUser(user, user1)
                        userRepository.save(user2)
                                .subscribe()
                    }
        } else Mono.error(ResponseStatusException(HttpStatus.BAD_REQUEST,"Provide a user id"))
    }

    fun getAllByDynamicFilter (user: Users): Flux<Users> {
        return userRepository.findAll(Example.of(user, ExampleMatcher.matchingAll().withIgnoreCase()), Sort.unsorted())
            .switchIfEmpty(userRepository.findAll(Example.of(user, ExampleMatcher.matchingAny())))
            .doOnError { error -> SqmNode.log.error("Failed to get Expense in dynamic query.", error)  }
    }

    fun find(id: Int): Mono<Users> {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No User found with Id: $id")))
    }

    fun findByUsername(username: String): Mono<Users> {
        return userRepository.findUserByUsername(username)
                .switchIfEmpty(Mono.error(Exception("No User found with username: $username")))
    }

    fun getAll(): Flux<Users> {
        return userRepository.findAll()
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.NOT_FOUND,"No Users found")))
    }

    fun findAllPaged(pageable: Pageable): Mono<Page<Users>> {
        return this.userRepository.count()
                .flatMap { userCount ->
                    this.userRepository.findAll(pageable.sort)
                            .buffer(pageable.pageSize, pageable.pageNumber + 1)
                            .elementAt(pageable.pageNumber, ArrayList())
                            .map { users -> PageImpl(users, pageable, userCount) }
                }
    }

    fun register(signUpRequest: SignupRequest): Mono<Users> {
        val user = Users();
        user.username = signUpRequest.username;
        user.password = encoder.encode(signUpRequest.password);
        user.name = signUpRequest.name;
        user.role = signUpRequest.role;
        user.email = signUpRequest.email;
        return userRepository.findUserByUsername(signUpRequest.username)
            .flatMap<Users?> { _ -> Mono.error((ResponseStatusException(HttpStatus.BAD_REQUEST,"Username already exists")))}
            .switchIfEmpty(userRepository.save(user)
                .switchIfEmpty(Mono.error(ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error saving user."))))
    }

    private fun updateUser(src: Users, dest: Users): Users {
        dest.username = src.username
        dest.name = src.name
        dest.password = encoder.encode(src.password)
        dest.email = src.email
        dest.role = src.role
        return dest
    }

}