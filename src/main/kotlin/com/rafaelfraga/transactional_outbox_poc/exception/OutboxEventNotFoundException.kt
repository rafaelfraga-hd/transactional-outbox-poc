package com.rafaelfraga.transactional_outbox_poc.exception

class OutboxEventNotFoundException(eventId: Long) :
    RuntimeException("Outbox event not found for id: $eventId")