package com.rafaelfraga.transactional_outbox_poc.domain

import jakarta.persistence.*
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "outbox_event")
class OutboxEvent(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "event_id", nullable = false, unique = true)
    val eventId: UUID,

    @Enumerated(EnumType.STRING)
    @Column(name = "aggregate_type", nullable = false, length = 50)
    val aggregateType: OutboxAggregateType,

    @Column(name = "aggregate_id", nullable = false)
    val aggregateId: Long,

    @Enumerated(EnumType.STRING)
    @Column(name = "event_type", nullable = false, length = 100)
    val eventType: OutboxEventType,

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "payload", nullable = false, columnDefinition = "jsonb")
    val payload: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    var status: OutboxStatus,

    @Column(name = "attempts", nullable = false)
    var attempts: Int = 0,

    @Column(name = "available_at", nullable = false)
    var availableAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "claimed_by")
    var claimedBy: String? = null,

    @Column(name = "claimed_at")
    var claimedAt: LocalDateTime? = null,

    @Column(name = "processed_at")
    var processedAt: LocalDateTime? = null,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "last_error")
    var lastError: String? = null
)