insert into webhook_registrations (target_url, secret, event_types)
values ('https://example.com/webhooks/order', 'local-dev-secret', 'order.created,order.confirmed')
on conflict do nothing;

