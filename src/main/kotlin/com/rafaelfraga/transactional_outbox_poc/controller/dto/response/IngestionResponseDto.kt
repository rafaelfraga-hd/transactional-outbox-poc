package com.rafaelfraga.transactional_outbox_poc.controller.dto.response

import com.rafaelfraga.transactional_outbox_poc.domain.IngestionStatus
import java.util.UUID

data class IngestionResponseDto(
    val protocol: UUID,
    val status: IngestionStatus,
    val created: Boolean
)