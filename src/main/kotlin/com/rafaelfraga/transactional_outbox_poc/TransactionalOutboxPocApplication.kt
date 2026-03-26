package com.rafaelfraga.transactional_outbox_poc

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionalOutboxPocApplication

fun main(args: Array<String>) {
	runApplication<TransactionalOutboxPocApplication>(*args)
}
