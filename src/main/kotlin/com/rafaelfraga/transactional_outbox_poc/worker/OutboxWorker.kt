package com.rafaelfraga.transactional_outbox_poc.worker

import com.rafaelfraga.transactional_outbox_poc.domain.OutboxStatus
import com.rafaelfraga.transactional_outbox_poc.repository.OutboxEventRepository
import com.rafaelfraga.transactional_outbox_poc.service.OutboxProcessingService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class OutboxWorker(
    private val outboxProcessingService: OutboxProcessingService
) {

    private val logger = LoggerFactory.getLogger(OutboxWorker::class.java)
    private val workerId = "worker-${UUID.randomUUID()}"

    @Scheduled(fixedDelay = 5000)
    fun processPendingEvents() {
        val claimedEventIds = outboxProcessingService
            .claimPendingEvents(
                batchSize = 10,
                workerId = workerId
            )

        if (claimedEventIds.isEmpty()) {
            logger.debug("No pending outbox events claimed by {}", workerId)
            return
        }

        logger.info("Worker {} claimed {} outbox events", workerId, claimedEventIds.size)

        claimedEventIds.forEach { eventId ->
            try {
                logger.info("Worker {} processing eventId={}", workerId, eventId)

                simulateProcessing()

                outboxProcessingService.completeProcessing(eventId)

                logger.info("Worker {} successfully processed eventId={}", workerId, eventId)
            } catch (ex: Exception) {
                logger.error(
                    "Worker {} failed processing eventId={}",
                    workerId,
                    eventId,
                    ex
                )
            }
        }
    }

    private fun simulateProcessing() {
        Thread.sleep(15000)
    }
}