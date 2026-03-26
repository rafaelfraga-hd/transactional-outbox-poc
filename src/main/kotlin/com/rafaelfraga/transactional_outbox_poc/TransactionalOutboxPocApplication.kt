package com.rafaelfraga.transactional_outbox_poc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
@ConfigurationPropertiesScan
class TransactionalOutboxPocApplication

fun main(args: Array<String>) {
	runApplication<TransactionalOutboxPocApplication>(*args)
}
