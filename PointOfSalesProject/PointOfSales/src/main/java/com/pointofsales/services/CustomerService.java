package com.pointofsales.services;

import com.pointofsales.entity.Customer;
import com.pointofsales.entity.Sale;
import com.pointofsales.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    public CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }


    public void addCustomer(Customer customer){
        customerRepository.save(customer);
    }

    public void removeCustomer(Customer customer){
        customerRepository.delete(customer);
    }

    public void updateCustomer(Customer customer){
        customer.setId(customer.getId());
        customerRepository.save(customer);
    }

}
