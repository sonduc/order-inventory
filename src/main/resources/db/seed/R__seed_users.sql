insert into users (username, password_hash, role, created_at, updated_at)
values
    ('admin', '{noop}admin123', 'ADMIN', now(), now()),
    ('customer', '{noop}customer123', 'CUSTOMER', now(), now())
on conflict do nothing;
