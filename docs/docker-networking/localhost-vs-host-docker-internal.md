# Docker Networking: localhost vs host.docker.internal

## Tại sao `localhost` vs `host.docker.internal`?

### 1. **Khi dùng DataGrid Tool từ Host Machine** → `localhost:5432`

- DataGrid (hay bất kỳ tool nào trên máy host) chạy **ngoài container**.
- PostgreSQL ở `local-infra` cũng chạy **trong container nhưng expose port** `5432:5432`.
- Từ host, bạn access qua `localhost:5432` vì từ khía cạnh OS, đó là địa chỉ của Docker daemon trên máy.

### 2. **Khi app chạy trong DevContainer** → `host.docker.internal:5432`

- Spring Boot app chạy **bên trong** devcontainer (container khác, không phải host).
- PostgreSQL ở `local-infra` chạy **cũng trong Docker nhưng là container riêng** (không phải container của devcontainer).
- Từ bên trong devcontainer, không thể dùng `localhost:5432` vì đó sẽ chỉ container chính nó, không phải host.
- Docker tự cung cấp **`host.docker.internal`** — một hostname ảo pointing tới Docker daemon/host machine.
- Vì vậy, từ devcontainer → `host.docker.internal:5432` → Docker engine → forward sang máy host → `localhost:5432` → PostgreSQL container ở `local-infra`.

### 3. **Sơ đồ mạng**

```
┌─ HOST MACHINE ──────────────────────────────────────┐
│                                                      │
│  DataGrid Tool (localhost:5432) ◄──┐              │
│                                      │              │
│  Docker Engine ─────────┐            │              │
│  ├─ order-inventory-dev  │           │              │
│  │   (devcontainer)      │           │              │
│  │   App code            │           │              │
│  │   (host.docker.       │─────────┐ │              │
│  │    internal:5432)     │         │ │              │
│  │                       │         │ │              │
│  └───────────────────────┘         │ │              │
│                                     ▼ ▼              │
│  ├─ local-infra-postgres          ◄──┐             │
│  │   PostgreSQL 16                   │              │
│  │   (localhost:5432)    ◄───────────┘              │
│  │                                                   │
│  └─ order-inventory-redis                           │
│     Redis 7                                          │
│                                                      │
└──────────────────────────────────────────────────────┘
```

### 4. **Ở application.yml của project**

```yaml
datasource:
  url: jdbc:postgresql://${DB_HOST:host.docker.internal}:${DB_PORT:5432}/${DB_NAME:order_inventory_db}
```

Default `host.docker.internal` **là đúng** vì app này sẽ **chạy trong devcontainer** (môi trường dev). Nếu sau này bạn deploy app này vào production (Docker runtime không phải devcontainer), biến env `DB_HOST` sẽ được set thành hostname service Postgres thực tế trong hệ thống đó.

## Kết luận

- **`localhost`** = "trên máy hiện tại" (dùng từ host)
- **`host.docker.internal`** = "Docker daemon của máy hiện tại" (dùng từ bên trong container để access các container khác hoặc dịch vụ trên host)

Cấu hình hiện tại của project hoàn toàn đúng, vì app chạy trong devcontainer nên cần `host.docker.internal`.
