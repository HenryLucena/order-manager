package com.example.ordermanager.item;

import com.example.ordermanager.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Order findByName(String name);
}
