package com.pointofsales.services;

import com.pointofsales.entity.Product;
import com.pointofsales.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    public ProductRepository productRepository;

    public ConfigurableApplicationContext applicationContext;

    @Autowired
    public ProductService(ProductRepository productRepository,  ConfigurableApplicationContext applicationContext) {
        this.productRepository = productRepository;
        this.applicationContext = applicationContext;
    }

    public void addProduct(Product product){
        productRepository.save(product);
    }

    public void removeProduct(Product product){
        productRepository.delete(product);
    }

    public void updateProduct(Product product){
        product.setId(product.getId());
        productRepository.save(product);
    }

    public List<Product> findAll(){
        return productRepository.findAll();

    }

    public List<Product> findProductByCategory(String category){
        return productRepository.getProductByCategory(category);
    }

    public Optional<Product> getProductByName(String name){
        return productRepository.getProductByName(name);
    }

    public Optional<Product> getProductById(Long id){
        return productRepository.findById(id);
    }


}
