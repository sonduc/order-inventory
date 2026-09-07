create table if not exists outbox_events (
    id bigserial primary key,
    aggregate_type varchar(100) not null,
    aggregate_id varchar(100) not null,
    event_name varchar(100) not null,
    payload text not null,
    published boolean not null default false,
    created_at timestamptz not null default now()
);

create index if not exists idx_outbox_events_published_created_at
    on outbox_events(published, created_at);

