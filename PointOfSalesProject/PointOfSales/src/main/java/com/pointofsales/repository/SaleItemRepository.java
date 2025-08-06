package com.pointofsales.repository;

import com.pointofsales.entity.Sale;
import com.pointofsales.entity.SaleItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleItemRepository extends JpaRepository<SaleItem, Long> {

    List<SaleItem> findSaleItemsByCustomer_Id(Long customer_id);

    List<SaleItem> findSaleItemsByProduct_Id(Long product_id);

    List<SaleItem> findSaleItemsBySale_Id(Long sale_id);
}
