create table outbox_event
(
    id             bigserial primary key,
    event_id       uuid         not null unique,
    aggregate_type varchar(50)  not null,
    aggregate_id   bigint       not null,
    event_type     varchar(100) not null,
    payload        jsonb        not null,
    status         varchar(30)  not null,
    attempts       int          not null default 0,
    available_at   timestamp    not null default now(),
    claimed_by     varchar(100) null,
    claimed_at     timestamp null,
    processed_at   timestamp null,
    created_at     timestamp    not null default now(),
    updated_at     timestamp    not null default now(),
    last_error     text null
);

create index idx_outbox_event_polling
    on outbox_event (status, available_at, created_at);

create index idx_outbox_event_aggregate
    on outbox_event (aggregate_type, aggregate_id);