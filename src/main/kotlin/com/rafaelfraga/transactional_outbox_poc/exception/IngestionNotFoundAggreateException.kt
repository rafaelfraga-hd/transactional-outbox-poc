package com.rafaelfraga.transactional_outbox_poc.exception

class IngestionNotFoundAggregateException(val aggregateId: Long) :
    RuntimeException("Ingestion request not found for aggregateId: ${aggregateId}")