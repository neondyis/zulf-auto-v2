package com.zulfauto.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories


@SpringBootApplication
@EnableR2dbcRepositories("com.zulfauto.backend.repositories")
class ZulfAutoApplication

fun main(args: Array<String>) {
    runApplication<ZulfAutoApplication>(*args)
}
