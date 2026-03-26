package com.rafaelfraga.transactional_outbox_poc.controller

import com.rafaelfraga.transactional_outbox_poc.controller.dto.request.IngestionRequestDto
import com.rafaelfraga.transactional_outbox_poc.controller.dto.response.IngestionResponseDto
import com.rafaelfraga.transactional_outbox_poc.service.IngestionService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/ingestions")
class IngestionController(
    private val service: IngestionService
) {

    @PostMapping
    fun createIngestion(
        @RequestHeader("Idempotency-Key") idempotencyKey: String,
        @Valid @RequestBody request: IngestionRequestDto
    ): ResponseEntity<IngestionResponseDto> {
        val respose = service.createIngestion(
            idempotencyKey,
            request
        )

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(respose)
    }

    @GetMapping("/{protocol}")
    fun getStatus(@PathVariable protocol: UUID): ResponseEntity<IngestionResponseDto> {
        val response = service.getStatus(protocol)
        return ResponseEntity.ok(response)
    }
}