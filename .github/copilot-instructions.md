# Copilot Instructions

## Build and test commands

- Start the app locally with the shared host Postgres expected by the devcontainer workflow:

  ```bash
  export DB_HOST=host.docker.internal
  export DB_PORT=5432
  export DB_NAME=order_inventory_db
  export DB_USERNAME=postgres
  export DB_PASSWORD=postgres
  mvn spring-boot:run
  ```

- Build the jar without running tests:

  ```bash
  mvn -DskipTests package
  ```

- Run the full test suite:

  ```bash
  mvn test
  ```

- Run a single unit test class:

  ```bash
  mvn -Dtest=ProductServiceTest test
  ```

- Run a single integration test class:

  ```bash
  mvn -Dtest=OrderFlowIntegrationTest test
  ```

- Run the containerized app stack defined in this repository:

  ```bash
  docker compose -f docker/docker-compose.yml up --build
  ```

There is no dedicated lint task configured in `pom.xml`.

## High-level architecture

This repository uses a five-layer package split under `com.example.orderinventory`:

- `config`: Spring configuration such as security, JWT properties, async, and scheduling enablement.
- `domain`: JPA entities, domain services, repository ports, domain events, business exceptions, and shared value objects.
- `application`: use-case orchestration, request/response DTOs, mappers, listeners, and schedulers.
- `infrastructure`: Spring Data JPA repositories, adapters that implement domain repository ports, JWT/security helpers, and the concrete event publisher.
- `presentation`: REST controllers, console commands, webhook endpoints, and API exception handlers.

Typical request flow is **controller -> application service -> domain service -> domain repository port -> infrastructure adapter -> Spring Data JPA**. Controllers stay thin and should return DTOs from the application layer rather than exposing entities directly.

Two cross-cutting business flows matter most:

- **Order creation** is orchestrated in `OrderApplicationService`: it snapshots product price/quantity into `OrderItem`, creates a pending order through `OrderFactory`, persists it through `OrderService`, then reserves stock by calling `ProductService.adjustStock(..., InventoryLockMode.PESSIMISTIC, ...)` for each line item.
- **Payment processing** is orchestrated in `PaymentApplicationService` and implemented in `PaymentService`: the mock gateway can fail, and failures cancel the order and restore stock inside the same service flow.
- **Security/authentication** is split between `AuthApplicationService` for login, `JwtTokenProvider` plus `JwtAuthenticationFilter` for token handling, and `SecurityAuthorityMapper` for converting `Role` permissions into Spring Security authorities.
- **Operational features** such as schedulers, console commands, listeners, and webhooks are intentionally wired through the application/presentation layers even though some modules are still scaffolds.

Persistence is schema-first. Flyway migrations and repeatable seeds in `src/main/resources/db/` own the database schema and seed data, while Hibernate runs with `ddl-auto=validate`.

Security is stateless JWT. `POST /api/auth/login` issues a token, `JwtAuthenticationFilter` parses the bearer token from the `Authorization` header, route rules live in `SecurityConfig`, and method-level checks use `@PreAuthorize`.

## Key conventions

- Keep repository abstractions in `domain/*Repository` and implement them in `infrastructure/persistence/adapter/*RepositoryAdapter`. Outside infrastructure, depend on the domain ports rather than Spring Data interfaces.

- Put orchestration and DTO mapping in the application layer. Domain services such as `ProductService`, `OrderService`, and `PaymentService` operate on entities and business rules; controllers should not reproduce that logic.

- Route every stock mutation through `ProductService.adjustStock`. That method is the repository-wide choke point for optimistic/pessimistic locking, inventory log persistence, and stock-related event publication.

- When using optimistic locking, pass `expectedVersion`; when guaranteed serialization is required, use `InventoryLockMode.PESSIMISTIC`. The repository already exposes `findByIdForUpdate()` for this path.

- Preserve the current transaction boundaries unless there is a clear reason to move them. Application services orchestrate multi-step use cases, while domain services also carry transactional behavior for aggregate-level operations and event emission.

- Domain events must go through the `EventPublisher` abstraction, not directly through Spring's `ApplicationEventPublisher`. The concrete `OrderEventPublisher` delays publication until `afterCommit()` when a transaction is active.

- Order state changes belong on the `Order` entity (`confirm`, `ship`, `complete`, `cancel`) and enforce business rules there. Keep invalid transition checks in the domain model, not in controllers.

- Avoid N+1 regressions on order reads by reusing the existing query shapes in `OrderJpaRepository`: `findDetailsById()` uses `@EntityGraph`, and `search()` uses `join fetch`.

- JPQL for the order aggregate must use the entity name `OrderAggregate`, not `Order`, because `domain.order.Order` is annotated with `@Entity(name = "OrderAggregate")`.

- If you change the schema, update Flyway SQL under `db/migration/`; if you change default local data, update repeatable seeds under `db/seed/`. Do not rely on Hibernate auto-creating tables.

- API errors should follow the existing `ApiError` response shape via `GlobalExceptionHandler`. Business exceptions map to structured HTTP responses; do not introduce ad hoc controller-level error payloads.

- Role and permission checks are dual-layered: `SecurityConfig` grants by route, while `@PreAuthorize` checks permission names such as `PRODUCT_WRITE` and `ORDER_READ`. When adding secured features, update `Permission`, `Role`, and any route rules together.

- JWT handling is custom rather than library-backed: `JwtTokenProvider` builds and verifies the token structure directly, and the login flow checks seeded password hashes via Spring's `PasswordEncoder`. Keep that in mind before swapping in framework defaults.

- Scheduler jobs are opt-in. `StockAlertScheduler` and `InventorySyncScheduler` only run when their `app.scheduling.*.enabled` flags are true.

- Console commands are also opt-in. `ConsoleCommandRunner` executes a bean implementing `ConsoleCommand` only when `app.console.enabled=true` and `app.console.command` matches the command bean name such as `stock:list`, `order:list`, or `system:ping`.

- Local defaults assume PostgreSQL 16. `application.yml` points to `host.docker.internal:5432` for the shared `local-infra` workflow, while `docker/docker-compose.yml` overrides `DB_HOST=postgres` for the in-repo compose stack. The app default port is `8081`.

- Flyway seed data creates two local users: `admin / admin123` and `customer / customer123`. Use those accounts when discussing or exercising JWT-protected endpoints unless the seeds have changed.

- Test coverage is intentionally uneven. `ProductServiceTest` and `OrderFlowIntegrationTest` are the main real examples today; many other classes under `src/test/java` are scaffolds marked `@Disabled`.

- Prefer adding focused tests alongside the existing patterns: pure domain/service logic in unit tests, database-backed flows in Spring Boot + Testcontainers integration tests, and only enable/expand the scaffolded suites when the implementation behind them becomes real.
