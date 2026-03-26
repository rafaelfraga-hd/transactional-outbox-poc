package com.rafaelfraga.transactional_outbox_poc.repository

import com.rafaelfraga.transactional_outbox_poc.domain.IngestionRequest
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface IngestionRequestRepository : JpaRepository<IngestionRequest, Long> {
    fun findByIdempotencyKey(idempotencyKey: String): IngestionRequest?
    fun findByProtocol(protocol: UUID): IngestionRequest?
}