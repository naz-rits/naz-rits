package com.pointofsales.services;

import com.pointofsales.entity.Cashier;
import com.pointofsales.repository.CashierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CashierService {

    public CashierRepository cashierRepository;

    @Autowired
    public CashierService(CashierRepository cashierRepository) {
        this.cashierRepository = cashierRepository;
    }


    public Cashier findCashierById(Long id) {
        return cashierRepository.findById(id).get();
    }

    public Optional<Cashier> findCashierByUsernameAndPassword(String username, String password) {
        return cashierRepository.findCashierByUsernameAndPassword(username, password);
    }

    public void addCashier(Cashier cashier) {
        cashierRepository.save(cashier);
    }

}
