package com.rafaelfraga.transactional_outbox_poc.service

import com.rafaelfraga.transactional_outbox_poc.controller.dto.request.IngestionRequestDto
import com.rafaelfraga.transactional_outbox_poc.controller.dto.response.IngestionResponseDto
import com.rafaelfraga.transactional_outbox_poc.domain.IngestionRequest
import com.rafaelfraga.transactional_outbox_poc.domain.IngestionStatus
import com.rafaelfraga.transactional_outbox_poc.domain.OutboxAggregateType
import com.rafaelfraga.transactional_outbox_poc.domain.OutboxEvent
import com.rafaelfraga.transactional_outbox_poc.domain.OutboxEventType
import com.rafaelfraga.transactional_outbox_poc.domain.OutboxStatus
import com.rafaelfraga.transactional_outbox_poc.exception.IngestionNotFoundException
import com.rafaelfraga.transactional_outbox_poc.repository.IngestionRequestRepository
import com.rafaelfraga.transactional_outbox_poc.repository.OutboxEventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class IngestionService(
    private val repository: IngestionRequestRepository,
    private val outboxRepository: OutboxEventRepository
) {

    @Transactional
    fun createIngestion(
        idempotencyKey: String,
        request: IngestionRequestDto
    ): IngestionResponseDto {

        val existingRequest = repository.findByIdempotencyKey(idempotencyKey);

        if (existingRequest != null) {
            return IngestionResponseDto(
                protocol = existingRequest.protocol,
                status = existingRequest.status,
                created = false
            )
        }

        val newRequest = IngestionRequest(
            protocol = UUID.randomUUID(),
            idempotencyKey = idempotencyKey,
            payload = request.payload,
            status = IngestionStatus.RECEIVED
        )

        val savedRequest = repository.save(newRequest)

        val outboxEvent = OutboxEvent(
            eventId = UUID.randomUUID(),
            aggregateType = OutboxAggregateType.INGESTION_REQUEST,
            aggregateId = savedRequest.id!!,
            eventType = OutboxEventType.INGESTION_RECEIVED,
            payload = savedRequest.payload,
            status = OutboxStatus.PENDING
        )

        outboxRepository.save(outboxEvent)

        return IngestionResponseDto(
            protocol = savedRequest.protocol,
            status = savedRequest.status,
            created = true
        )
    }

    fun getStatus(protocol: UUID): IngestionResponseDto {
        val request = repository.findByProtocol(protocol)
            ?: throw IngestionNotFoundException(protocol)

        return IngestionResponseDto(
            protocol = request.protocol,
            status = request.status,
            created = false
        )
    }
}