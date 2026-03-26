create table ingestion_request
(
    id              bigserial primary key,
    protocol        uuid         not null unique,
    idempotency_key varchar(100) not null unique,
    payload         jsonb        not null,
    status          varchar(30)  not null,
    created_at      timestamp    not null default now(),
    updated_at      timestamp    not null default now(),
    processed_at    timestamp null,
    error_message   text null
);

create index idx_ingestion_request_status
    on ingestion_request (status);