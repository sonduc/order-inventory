insert into products (name, description, price, stock_quantity, version, created_at, updated_at)
values
    ('Starter Keyboard', 'Seed product for local development', 99.90, 10, 0, now(), now()),
    ('Starter Mouse', 'Seed product for local development', 49.90, 25, 0, now(), now())
on conflict do nothing;

