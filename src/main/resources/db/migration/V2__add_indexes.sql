create index if not exists idx_products_name on products(name);
create index if not exists idx_inventory_logs_product_id on inventory_logs(product_id);
create index if not exists idx_orders_customer_id on orders(customer_id);
create index if not exists idx_orders_status_created_at on orders(status, created_at desc);
create index if not exists idx_order_items_order_id on order_items(order_id);
create index if not exists idx_payments_order_id on payments(order_id);

