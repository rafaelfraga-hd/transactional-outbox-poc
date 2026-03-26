package com.rafaelfraga.transactional_outbox_poc.controller.dto.request

import jakarta.validation.constraints.NotBlank

data class IngestionRequestDto(
    @field:NotBlank
    val payload: String
)