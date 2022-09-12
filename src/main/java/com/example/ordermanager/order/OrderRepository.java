package com.example.ordermanager.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            value = "SELECT * FROM orders o " +
                    "WHERE o.order_status = 0 " +
                    "ORDER BY creation_date " +
                    "limit 1",
            nativeQuery = true
    )
    Order getOrderPending();
}
