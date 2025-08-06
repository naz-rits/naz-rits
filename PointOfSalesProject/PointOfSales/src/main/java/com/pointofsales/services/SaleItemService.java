package com.pointofsales.services;

import com.pointofsales.entity.Sale;
import com.pointofsales.entity.SaleItem;
import com.pointofsales.repository.SaleItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleItemService {

    public SaleItemRepository saleItemRepository;

    public SaleItemService(SaleItemRepository saleItemRepository) {
        this.saleItemRepository = saleItemRepository;
    }

    public void addSaleItem(SaleItem saleItem){
        saleItemRepository.save(saleItem);
    }

    public void removeSaleItem(SaleItem saleItem){
        saleItemRepository.delete(saleItem);
    }

    public void updateSaleItem(SaleItem saleItem){
        saleItem.setId(saleItem.getId());
        saleItemRepository.save(saleItem);
    }

    public List<SaleItem> getSaleItemByCustomerId(Long customerId){
        List<SaleItem> getSaleItemByCustomer = saleItemRepository.findSaleItemsByCustomer_Id(customerId);
        return getSaleItemByCustomer;
    }

    public List<SaleItem> getSaleItemByProduct_Id(Long productId){
        List<SaleItem> getSaleItemByProduct = saleItemRepository.findSaleItemsByProduct_Id(productId);
        return getSaleItemByProduct;
    }

    public List<SaleItem> getSaleItemBySale_Id(Long saleId){
        List<SaleItem> getSaleItemBySale = saleItemRepository.findSaleItemsBySale_Id(saleId);
        return getSaleItemBySale;
    }


}
