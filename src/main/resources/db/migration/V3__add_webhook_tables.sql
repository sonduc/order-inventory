create table if not exists webhook_registrations (
    id bigserial primary key,
    target_url varchar(500) not null,
    secret varchar(255),
    event_types varchar(500),
    created_at timestamptz not null default now()
);

create table if not exists webhook_delivery_logs (
    id bigserial primary key,
    target_url varchar(500) not null,
    event_type varchar(100) not null,
    successful boolean not null,
    attempts integer not null,
    attempted_at timestamptz not null
);

