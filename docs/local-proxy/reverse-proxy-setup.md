# Cài đặt Local Reverse Proxy: order-inventory.local

## Tổng quan

Thư mục `~/code/local-proxy` chứa một Nginx reverse proxy dùng chung cho tất cả các dự án local. Nó cho phép truy cập các dự án phát triển qua tên miền thân thiện như `order-inventory.local` thay vì phải dùng IP address và port number.

## Cách hoạt động của order-inventory.local

Quy trình gồm 3 lớp:

### Lớp 1: DNS Resolution (Máy host)

Trong file `/etc/hosts` trên máy local của bạn:
```
127.0.0.1 order-inventory.local
```

Khi bạn gõ `order-inventory.local` vào trình duyệt, OS DNS resolver chuyển nó thành `127.0.0.1`.

### Lớp 2: Nginx Reverse Proxy (localhost:80)

Nginx container chạy trên port 80 (cổng HTTP mặc định):
- **Tên container:** `local-proxy-nginx`
- **File cấu hình:** `/code/local-proxy/nginx.conf`
- **Lắng nghe trên:** Port 80
- **Server block:** Khớp với `server_name order-inventory.local`

Khi request đến `order-inventory.local:80`, Nginx sẽ khớp nó với server block và chuyển tiếp tới backend.

### Lớp 3: Ứng dụng Backend (Spring Boot)

Directive `proxy_pass` của Nginx chuyển tiếp tới:
```
http://host.docker.internal:8081
```

- `host.docker.internal` = Hostname đặc biệt để từ bên trong container có thể kết nối tới máy host
- `8081` = Cổng mặc định của ứng dụng Spring Boot
- Nginx giữ lại header `Host: order-inventory.local` gốc thông qua `proxy_set_header Host $host`

## Sơ đồ Request Flow

```
┌────────────────────────────────────────────────────────┐
│ Trình duyệt của bạn (trên máy host)                    │
│ GET http://order-inventory.local/api/products          │
└─────────────────────┬────────────────────────────────┘
                      │
                      ↓
         ┌───────────────────────────┐
         │ DNS Resolver              │
         │ order-inventory.local     │
         │ → 127.0.0.1:80            │
         └────────────┬──────────────┘
                      │
                      ↓
   ┌──────────────────────────────────────────┐
   │ Nginx Container (local-proxy)            │
   │ Port 80, khớp server_name                │
   │ proxy_pass → host.docker.internal        │
   └────────────┬─────────────────────────────┘
                │
                ↓
     ┌──────────────────────────────┐
     │ Docker Host                  │
     │ host.docker.internal:8081    │
     └────────────┬─────────────────┘
                  │
                  ↓
         ┌────────────────────┐
         │ Spring Boot App    │
         │ Port 8081          │
         │ (DevContainer)     │
         └────────────────────┘
```

## Tại sao cần setup này?

### Vấn đề nếu không có proxy

1. **Từ trình duyệt (máy host):** Không thể dùng `localhost:8081` khi app ở bên trong DevContainer
   - `localhost` trên máy host ≠ app bên trong container
   - Cần dùng `host.docker.internal:8081`, nhưng đó không phải là tên DNS thân thiện

2. **Từ bên trong container:** Cũng không thể dùng `localhost:8081`
   - `localhost` bên trong container = chính container đó, không phải máy host
   - Phải dùng `host.docker.internal:8081`

### Giải pháp: Reverse Proxy

- **Máy host:** Người dùng vào domain thân thiện `order-inventory.local`
- **DNS:** Phân giải thành `127.0.0.1:80` (Nginx local)
- **Nginx:** Chuyển tiếp tới `host.docker.internal:8081` (nơi app chạy)
- **Kết quả:** Truy cập DNS-friendly mượt mà từ trình duyệt dù app ở đâu

## Chi tiết cấu hình

### Nginx Config (`nginx.conf`)

```nginx
server {
    listen 80;
    server_name order-inventory.local;

    location / {
        proxy_pass http://host.docker.internal:8081;
        proxy_http_version 1.1;
        proxy_set_header Host $host;                      # Giữ nguyên Host header gốc
        proxy_set_header X-Real-IP $remote_addr;          # IP client thật
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;       # Giữ http/https
    }
}
```

Header quan trọng:
- `Host: order-inventory.local` - App biết request gốc đến domain này
- `X-Real-IP` / `X-Forwarded-For` - App có thể log/track IP client thật
- `X-Forwarded-Proto` - Quan trọng với app kiểm tra protocol (http vs https)

### Docker Compose Setup

```yaml
services:
  nginx:
    image: nginx:1.27-alpine
    container_name: local-proxy-nginx
    restart: unless-stopped
    ports:
      - "${NGINX_PORT:-80}:80"
    volumes:
      - ./nginx.conf:/etc/nginx/nginx.conf:ro
    extra_hosts:
      - "host.docker.internal:host-gateway"
```

`extra_hosts` đảm bảo Nginx container có thể phân giải `host.docker.internal` đúng cách.

## Các bước cài đặt

1. **Thêm hosts entry** (một lần duy nhất, trên máy host):
   ```bash
   echo "127.0.0.1 order-inventory.local" | sudo tee -a /etc/hosts
   ```
   (Trên Windows: Chỉnh sửa `C:\Windows\System32\drivers\etc\hosts`)

2. **Khởi động proxy** (từ thư mục `~/code/local-proxy`):
   ```bash
   docker compose up -d
   ```

3. **Truy cập từ trình duyệt:**
   ```
   http://order-inventory.local
   ```

## Thêm dự án mới

Để thêm dự án khác (ví dụ: `another-project.local`):

1. Thêm hosts entry:
   ```
   127.0.0.1 another-project.local
   ```

2. Thêm `server {}` block mới vào `nginx.conf`:
   ```nginx
   server {
       listen 80;
       server_name another-project.local;
       
       location / {
           proxy_pass http://host.docker.internal:8082;  # Port khác
           proxy_set_header Host $host;
           proxy_set_header X-Real-IP $remote_addr;
           proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
           proxy_set_header X-Forwarded-Proto $scheme;
       }
   }
   ```

3. Reload Nginx:
   ```bash
   docker compose exec nginx nginx -s reload
   ```

## Những điểm quan trọng cần nhớ

- **Từ máy host:** Dùng `order-inventory.local` (tên DNS thân thiện)
- **Từ trình duyệt:** Dùng `order-inventory.local` (tên DNS thân thiện)
- **Từ bên trong DevContainer:** Nếu truy cập backend trực tiếp (ví dụ: test code) thì vẫn dùng `host.docker.internal:8081`, **không dùng tên domain**
- **Port ứng dụng Spring Boot:** Phải là `8081` (cấu hình trong `application.yml`)
- **Nginx container:** Phải chạy liên tục để domain routing hoạt động
- **Host entry:** Cần trên mỗi máy muốn truy cập development domain

## Khắc phục sự cố

| Vấn đề | Nguyên nhân | Giải pháp |
|--------|-----------|----------|
| `order-inventory.local` không phân giải | DNS entry thiếu | Thêm vào `/etc/hosts` trên máy host |
| Connection refused trên `order-inventory.local:80` | Nginx không chạy | `docker compose -f ~/code/local-proxy/docker-compose.yml up -d` |
| Không kết nối được backend | Nginx chuyển tiếp sai port | Kiểm tra app chạy trên `:8081` và `proxy_pass` khớp |
| Host header sai trong logs | Header không được chuyển tiếp | Kiểm tra `proxy_set_header Host $host` trong nginx.conf |
| Hoạt động trên host nhưng không ở container | DNS container có vấn đề | Container nên truy cập backend qua `host.docker.internal:8081`, không dùng domain |
