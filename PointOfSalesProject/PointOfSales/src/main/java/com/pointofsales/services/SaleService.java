package com.pointofsales.services;

import com.pointofsales.entity.Sale;
import com.pointofsales.repository.SaleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    public SaleRepository saleRepository;

    public SaleService(SaleRepository saleRepository) {
        this.saleRepository = saleRepository;
    }

    public void addSale(Sale sale){
        saleRepository.save(sale);
    }

    public void removeSale(Sale sale){
        saleRepository.delete(sale);
    }

    public void updateSale(Sale sale){
        sale.setId(sale.getId());
        saleRepository.save(sale);
    }

    public List<Sale> findSaleByCustomerId(Long customerId){
        List<Sale> customerOrders = saleRepository.findSaleByCustomer_Id(customerId);

        return customerOrders;
    }
}
