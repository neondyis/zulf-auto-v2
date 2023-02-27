package com.zulfauto.backend.services
import com.zulfauto.backend.repositories.UserRepository
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono


@Service
class CustomReactiveUserDetailsService(private val userRepository: UserRepository) : ReactiveUserDetailsService {

    override fun findByUsername(username: String?): Mono<UserDetails> = mono {
        val user = userRepository.findUserByUsername(username!!).awaitFirstOrNull() ?: throw BadCredentialsException("Invalid Credentials")
        return@mono User(user.username,user.password, listOf(user))
    }
}
