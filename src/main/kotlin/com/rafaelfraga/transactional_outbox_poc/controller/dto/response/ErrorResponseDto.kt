package com.rafaelfraga.transactional_outbox_poc.controller.dto.response

data class ErrorResponseDto(
    val message: String,
    val path: String,
    val timestamp: String
)