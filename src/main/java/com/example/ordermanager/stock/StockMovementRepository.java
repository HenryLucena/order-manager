package com.example.ordermanager.stock;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {
    @Query(
            value = "SELECT * FROM stock_movement sm " +
                    "where sm.item_id = ?1 " +
                    "and sm.current_quantity > 0 " +
                    "order by sm.creation_date " +
                    "limit 1",
            nativeQuery = true
    )
    StockMovement getAvaliableStock(long itemId);
}
