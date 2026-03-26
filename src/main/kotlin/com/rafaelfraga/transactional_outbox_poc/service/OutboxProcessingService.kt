package com.rafaelfraga.transactional_outbox_poc.service

import com.rafaelfraga.transactional_outbox_poc.domain.IngestionStatus
import com.rafaelfraga.transactional_outbox_poc.domain.OutboxStatus
import com.rafaelfraga.transactional_outbox_poc.exception.IngestionNotFoundAggregateException
import com.rafaelfraga.transactional_outbox_poc.exception.OutboxEventNotFoundException
import com.rafaelfraga.transactional_outbox_poc.repository.IngestionRequestRepository
import com.rafaelfraga.transactional_outbox_poc.repository.OutboxEventRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OutboxProcessingService(
    private val outboxEventRepository: OutboxEventRepository,
    private val ingestionRequestRepository: IngestionRequestRepository
) {

    @Transactional
    fun markAsProcessing(eventId: Long) {
        val event = outboxEventRepository.findById(eventId)
            .orElseThrow {
                OutboxEventNotFoundException(eventId)
            }

        val ingestionRequest = ingestionRequestRepository.findById(event.aggregateId)
            .orElseThrow {
                IngestionNotFoundAggregateException(event.aggregateId)
            }

        event.status = OutboxStatus.PROCESSING
        event.claimedBy = "local-worker"
        event.claimedAt = LocalDateTime.now()
        event.updatedAt = LocalDateTime.now()

        ingestionRequest.status = IngestionStatus.PROCESSING
        ingestionRequest.updatedAt = LocalDateTime.now()
    }

    @Transactional
    fun completeProcessing(eventId: Long) {
        val event = outboxEventRepository.findById(eventId)
            .orElseThrow {
                OutboxEventNotFoundException(eventId)
            }

        val ingestionRequest = ingestionRequestRepository.findById(event.aggregateId)
            .orElseThrow {
                IngestionNotFoundAggregateException(event.aggregateId)
            }

        event.status = OutboxStatus.PROCESSED
        event.processedAt = LocalDateTime.now()
        event.updatedAt = LocalDateTime.now()

        ingestionRequest.status = IngestionStatus.PROCESSED
        ingestionRequest.processedAt = LocalDateTime.now()
        ingestionRequest.updatedAt = LocalDateTime.now()
    }
}