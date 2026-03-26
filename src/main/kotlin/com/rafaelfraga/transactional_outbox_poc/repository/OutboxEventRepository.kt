package com.rafaelfraga.transactional_outbox_poc.repository

import com.rafaelfraga.transactional_outbox_poc.domain.OutboxEvent
import org.springframework.data.jpa.repository.JpaRepository

interface OutboxEventRepository : JpaRepository<OutboxEvent, Long>{
}