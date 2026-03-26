package com.rafaelfraga.transactional_outbox_poc.exception

import java.util.UUID

class IngestionNotFoundException(protocol: UUID) :
    RuntimeException("Ingestion not found for protocol: $protocol")