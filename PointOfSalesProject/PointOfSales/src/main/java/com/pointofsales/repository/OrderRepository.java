package com.pointofsales.repository;

import com.pointofsales.entity.pendingorders_completed.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o ORDER BY o.date ASC, o.time ASC")
    List<Order> findAllOrderByDateAndTime();

}
