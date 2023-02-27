package com.zulfauto.backend

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions
import org.springframework.data.r2dbc.mapping.R2dbcMappingContext
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories
import org.springframework.data.relational.core.mapping.NamingStrategy
import org.springframework.util.Assert
import java.util.*


@SpringBootApplication
@EnableR2dbcRepositories("com.zulfauto.backend.repositories")
class ZulfAutoApplication

fun main(args: Array<String>) {
    runApplication<ZulfAutoApplication>(*args)
}
