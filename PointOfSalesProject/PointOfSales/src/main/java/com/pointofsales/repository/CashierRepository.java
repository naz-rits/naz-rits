package com.pointofsales.repository;

import com.pointofsales.entity.Cashier;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CashierRepository extends JpaRepository<Cashier, Long> {
    Optional<Cashier> findCashierByUsernameAndPassword(String username, String password);
}
