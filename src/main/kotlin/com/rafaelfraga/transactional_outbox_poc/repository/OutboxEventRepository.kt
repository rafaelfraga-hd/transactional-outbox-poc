package com.rafaelfraga.transactional_outbox_poc.repository

import com.rafaelfraga.transactional_outbox_poc.domain.OutboxEvent
import com.rafaelfraga.transactional_outbox_poc.domain.OutboxStatus
import org.springframework.data.jpa.repository.JpaRepository

interface OutboxEventRepository : JpaRepository<OutboxEvent, Long>{
    fun findTop10ByStatusOrderByCreatedAtAsc(status: OutboxStatus): List<OutboxEvent>
}