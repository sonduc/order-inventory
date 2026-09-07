package com.example.orderinventory.infrastructure.persistence.jpa;

import com.example.orderinventory.domain.order.Order;
import com.example.orderinventory.domain.order.OrderStatus;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"items", "items.product"})
    @Query("select o from OrderAggregate o where o.id = :orderId")
    Optional<Order> findDetailsById(@Param("orderId") Long orderId);

    @Query("""
            select distinct o
            from OrderAggregate o
            left join fetch o.items i
            left join fetch i.product
            where (:status is null or o.status = :status)
              and (:customerId is null or o.customerId = :customerId)
              and (:fromTime is null or o.createdAt >= :fromTime)
              and (:toTime is null or o.createdAt <= :toTime)
            order by o.createdAt desc
            """)
    List<Order> search(
            @Param("status") OrderStatus status,
            @Param("customerId") Long customerId,
            @Param("fromTime") Instant fromTime,
            @Param("toTime") Instant toTime
    );
}

