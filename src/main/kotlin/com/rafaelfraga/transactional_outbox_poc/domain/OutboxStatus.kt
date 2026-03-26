package com.rafaelfraga.transactional_outbox_poc.domain

enum class OutboxStatus {
    PENDING,
    PROCESSING,
    PROCESSED,
    FAILED
}