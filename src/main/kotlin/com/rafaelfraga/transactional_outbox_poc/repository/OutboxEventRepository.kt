package com.rafaelfraga.transactional_outbox_poc.repository

import com.rafaelfraga.transactional_outbox_poc.domain.OutboxEvent
import com.rafaelfraga.transactional_outbox_poc.domain.OutboxStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface OutboxEventRepository : JpaRepository<OutboxEvent, Long>{
    fun findTop10ByStatusOrderByCreatedAtAsc(status: OutboxStatus): List<OutboxEvent>

    @Query(
        value = """
            select *
            from outbox_event
            where id in (
                select id
                from outbox_event
                where status = 'PENDING'
                  and available_at <= now()
                order by created_at asc
                limit :batchSize
                for update skip locked
            )
        """,
            nativeQuery = true
    )
    fun claimPendingEvents(@Param("batchSize") batchSize: Int): List<OutboxEvent>
}