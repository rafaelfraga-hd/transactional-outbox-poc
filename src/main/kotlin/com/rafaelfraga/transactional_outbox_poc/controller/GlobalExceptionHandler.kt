package com.rafaelfraga.transactional_outbox_poc.controller

import com.rafaelfraga.transactional_outbox_poc.exception.IngestionNotFoundException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(IngestionNotFoundException::class)
    fun handleNotFound(ex: IngestionNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(404).body(
            mapOf("message" to ex.message)
        )
    }
}