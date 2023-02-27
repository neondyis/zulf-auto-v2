package com.zulfauto.backend.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource

import org.springframework.web.cors.reactive.CorsWebFilter
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource


@Configuration
class PreFlightCorsConfiguration {

    @Bean
    fun corsFilter(): CorsWebFilter {
        return CorsWebFilter(corsConfigurationSource())
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration().applyPermitDefaultValues()
        config.addExposedHeader(
                "Authorization, x-xsrf-token, Access-Control-Allow-Headers, Origin, Accept, X-Requested-With, "
                        + "Content-Type, Access-Control-Request-Method, Custom-Filter-Header");
        config.addAllowedHeader("*");
        config.addAllowedMethod(HttpMethod.PUT)
        config.addAllowedMethod(HttpMethod.GET)
        config.addAllowedMethod(HttpMethod.PATCH)
        config.addAllowedMethod(HttpMethod.POST)
        config.addAllowedMethod(HttpMethod.TRACE)
        config.addAllowedMethod(HttpMethod.OPTIONS)
        config.addAllowedMethod(HttpMethod.DELETE)
        source.registerCorsConfiguration("/**", config)
        return source
    }

}