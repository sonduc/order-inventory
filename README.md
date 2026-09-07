# Order Inventory

Project scaffold theo hướng **layered architecture / DDD-lite** để phù hợp hơn với bài luyện tập ở mức middle trở lên: domain rõ ràng, application service tách riêng, infrastructure adapter riêng, presentation riêng.

## Trạng thái hiện tại

Phần này đã được **nâng cấp từ skeleton CRUD phẳng** sang kiến trúc nhiều lớp:

- `domain/` chứa entity, business service, event, exception, value object, repository port
- `application/` chứa use case, DTO, mapper, listener, scheduler
- `infrastructure/` chứa JPA repository, adapter, security, webhook, messaging, scheduler helper
- `presentation/` chứa REST controller, exception handler, console/webhook endpoint shell
- `resources/` đã có profile config, migration/seed scaffold, template scaffold, logback config
- `docker/` và `scripts/` được tách riêng thay vì để lẫn ở root

## Những phần đã có logic thật

1. **Product / Inventory**
   - CRUD sản phẩm
   - stock adjustment
   - optimistic lock bằng `@Version`
   - chọn được `OPTIMISTIC` hoặc `PESSIMISTIC` lock mode khi chỉnh stock
   - inventory log entity
   - domain event khi tăng/giảm/hết stock

2. **Order**
   - `Order`, `OrderItem`, `OrderStatus`
   - `OrderFactory`
   - `OrderService`
   - tạo order trong transaction và reserve stock bằng pessimistic lock
   - search query và `@EntityGraph` ở persistence layer
   - REST endpoint tạo / xem / search / đổi state cơ bản

3. **Payment**
   - `PaymentService`
   - `MockPaymentGateway`
   - payment fail sẽ cancel order và hoàn stock trong cùng flow
   - event success/fail
   - endpoint gọi payment giả lập

4. **Security**
   - `POST /api/auth/login` trả JWT
   - `JwtAuthenticationFilter` parse token từ `Authorization: Bearer ...`
   - role `ADMIN` / `CUSTOMER`
   - permission `PRODUCT_WRITE`, `ORDER_READ`, `ORDER_WRITE`, `PAYMENT_PROCESS`, `WEBHOOK_MANAGE`
   - route protection bằng `@PreAuthorize`

5. **Scheduler / Console**
   - scheduler có cả `cron` và `fixedDelay`
   - console command runner bằng `app.console.command`

## Những phần đang là scaffold để bạn hoàn thiện ở vòng tiếp theo

- webhook persistence/outbox thật
- concurrency test 2 request mua cùng 1 stock
- Testcontainers e2e đủ bài
- fine-grained authorization kiểu "customer chỉ xem order của chính mình"
- console UX nâng cấp lên Spring Shell nếu muốn

## Cấu trúc chính

```text
order-inventory/
├── .devcontainer/devcontainer.json
├── docker/
│   ├── Dockerfile
│   ├── docker-compose.yml
│   └── docker-compose.test.yml
├── scripts/
│   ├── deploy.sh
│   ├── backup-db.sh
│   └── load-test/
├── src/
│   ├── main/
│   │   ├── java/com/example/orderinventory/
│   │   │   ├── config/
│   │   │   ├── domain/
│   │   │   ├── application/
│   │   │   ├── infrastructure/
│   │   │   └── presentation/
│   │   └── resources/
│   └── test/java/com/example/orderinventory/
├── .env.example
├── pom.xml
└── README.md
```

## Chạy local

1. Mở project bằng VS Code.
2. Chọn **Dev Containers: Reopen in Container**.
3. Export env nếu DB nằm ở `local-infra` trên máy host:

   ```bash
   export DB_HOST=host.docker.internal
   export DB_PORT=5432
   export DB_NAME=order_inventory_db
   export DB_USERNAME=postgres
   export DB_PASSWORD=postgres
   ```

4. Chạy app:

   ```bash
   mvn spring-boot:run
   ```

## Tài khoản local seed sẵn

- `admin / admin123`
- `customer / customer123`

## Login lấy JWT

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H 'Content-Type: application/json' \
  -d '{"username":"admin","password":"admin123"}'
```

## Chạy console command

```bash
mvn spring-boot:run \
  -Dspring-boot.run.arguments="--app.console.enabled=true,--app.console.command=stock:list"
```

## Chạy Docker Compose

```bash
docker compose -f docker/docker-compose.yml up --build
```

## Endpoint đang có sẵn

| Method | Path | Mục đích |
|---|---|---|
| GET | `/api/products` | List sản phẩm |
| GET | `/api/products/{id}` | Xem chi tiết sản phẩm |
| POST | `/api/admin/products` | Tạo sản phẩm |
| PUT | `/api/admin/products/{id}` | Cập nhật sản phẩm |
| PATCH | `/api/admin/products/{id}/stock` | Cộng/trừ tồn kho, hỗ trợ lock mode |
| POST | `/api/orders` | Tạo order + reserve stock |
| GET | `/api/orders/{id}` | Xem order |
| GET | `/api/orders` | Search order |
| POST | `/api/orders/{id}/confirm` | Confirm order |
| POST | `/api/orders/{id}/cancel` | Cancel order |
| POST | `/api/orders/{id}/ship` | Ship order |
| POST | `/api/payments` | Trigger mock payment |
| POST | `/api/webhooks/test-delivery` | Gửi thử webhook |
| POST | `/api/auth/login` | Đăng nhập lấy JWT |
| GET | `/api/auth/status` | Kiểm tra auth đã bật |
| GET | `/actuator/health` | Health check |

## Gợi ý vòng tiếp theo

1. Viết race condition integration test để chứng minh chỉ 1 request thắng khi stock = 1.
2. Thêm ownership authorization cho `CUSTOMER` xem đúng order của mình.
3. Hoàn thiện webhook persistence/outbox và retry policy thực sự.
4. Nếu muốn trải nghiệm CLI đẹp hơn, nâng console runner hiện tại lên Spring Shell.
