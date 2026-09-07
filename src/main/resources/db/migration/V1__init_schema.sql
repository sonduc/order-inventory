create table if not exists products (
    id bigserial primary key,
    name varchar(255) not null,
    description varchar(2000),
    price numeric(19, 2) not null,
    stock_quantity integer not null,
    version bigint,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table if not exists inventory_logs (
    id bigserial primary key,
    product_id bigint not null,
    change_amount integer not null,
    remaining_stock integer not null,
    reason varchar(255) not null,
    created_at timestamptz not null
);

create table if not exists orders (
    id bigserial primary key,
    customer_id bigint not null,
    status varchar(30) not null,
    total_amount numeric(19, 2) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

create table if not exists order_items (
    id bigserial primary key,
    order_id bigint not null references orders(id) on delete cascade,
    product_id bigint not null references products(id),
    quantity integer not null,
    price_at_order_time numeric(19, 2) not null
);

create table if not exists payments (
    id bigserial primary key,
    order_id bigint not null unique references orders(id),
    status varchar(30) not null,
    attempted_at timestamptz not null,
    provider_reference varchar(255)
);

create table if not exists users (
    id bigserial primary key,
    username varchar(100) not null unique,
    password_hash varchar(255) not null,
    role varchar(30) not null,
    created_at timestamptz not null,
    updated_at timestamptz not null
);

