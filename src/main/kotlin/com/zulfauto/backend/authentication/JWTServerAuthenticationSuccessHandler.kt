package com.zulfauto.backend.authentication

import com.zulfauto.backend.util.HttpExceptionFactory.unauthorized
import kotlinx.coroutines.reactor.awaitSingleOrNull
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class JWTServerAuthenticationSuccessHandler(
    private val jwtService: JWTService,
) : ServerAuthenticationSuccessHandler {

    private val TWENTY_FOUR_HOURS = 1000 * 60 * 60 * 24
    private val FOUR_HOURS = 1000 * 60 * 60 * 4

    // TODO Implement refresh token usage

    override fun onAuthenticationSuccess(webFilterExchange: WebFilterExchange, authentication: Authentication): Mono<Void> = mono {
        val principal = authentication.principal ?: throw unauthorized()
        val exchange = webFilterExchange.exchange

        when (principal) {
            is User -> {
                val roles = principal.authorities.map { it.authority }.toTypedArray();
                val accessToken = jwtService.accessToken(principal.username!!, FOUR_HOURS, roles)
                val refreshToken = jwtService.refreshToken(principal.username!!, TWENTY_FOUR_HOURS, roles)
                exchange.response.headers.set("Authorization", accessToken)
                exchange.response.headers.set("JWT-Refresh-Token", refreshToken)
            }
        }

        return@mono exchange.response.setComplete().awaitSingleOrNull()
    }
}
