package com.rafaelfraga.transactional_outbox_poc.worker

import com.rafaelfraga.transactional_outbox_poc.domain.OutboxStatus
import com.rafaelfraga.transactional_outbox_poc.repository.OutboxEventRepository
import com.rafaelfraga.transactional_outbox_poc.service.OutboxProcessingService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class OutboxWorker(
    private val outboxEventRepository: OutboxEventRepository,
    private val outboxProcessingService: OutboxProcessingService
) {

    private val logger = LoggerFactory.getLogger(OutboxWorker::class.java)

    @Scheduled(fixedDelay = 5000)
    fun processPendingEvents() {
        val events = outboxEventRepository
            .findTop10ByStatusOrderByCreatedAtAsc(OutboxStatus.PENDING)

        if (events.isEmpty()) {
            logger.debug("No pending outbox events found")
            return
        }

        logger.info("Processing {} outbox events", events.size)

        events.forEach { event ->
            try {
                logger.info("Processing eventId={}", event.id)

                outboxProcessingService.markAsProcessing(event.id!!)

                simulateProcessing()

                outboxProcessingService.completeProcessing(event.id!!)

                logger.info("Processed eventId={}", event.id)

            } catch (ex: Exception) {
                logger.error(
                    "Failed processing eventId={}, aggregateId={}",
                    event.id,
                    event.aggregateId,
                    ex
                )
            }
        }
    }

    private fun simulateProcessing() {
        Thread.sleep(15000)
    }
}