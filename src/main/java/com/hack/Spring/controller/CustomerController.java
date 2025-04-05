package com.hack.Spring.controller;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hack.Spring.dto.CustomerDto;

@RestController
@RequestMapping("/api")
public class CustomerController {

    
    @GetMapping
    public List<CustomerDto> getAllCustomers() {
        List<CustomerDto> customers = new ArrayList<>();

        customers.add(createCustomer("Любит скидки", -5, 0, 8));
        customers.add(createCustomer("Покупает электронику", 0, 0, 8));
        customers.add(createCustomer("Покупает химию", 5, 0, 8));

        return customers;
    }

    private CustomerDto createCustomer(String name, double x, double y, double z) {
        CustomerDto dto = new CustomerDto();
        dto.setName(name);
        dto.setX(x);
        dto.setY(y);
        dto.setZ(z);
        return dto;
    }
}
