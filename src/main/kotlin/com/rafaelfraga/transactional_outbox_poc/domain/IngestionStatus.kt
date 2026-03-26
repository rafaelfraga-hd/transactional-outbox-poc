package com.rafaelfraga.transactional_outbox_poc.domain

enum class IngestionStatus {
    RECEIVED,
    PROCESSING,
    PROCESSED,
    FAILED
}