package com.hack.Spring.controller;

import com.hack.Spring.model.ObjLocation;
import com.hack.Spring.repository.ObjLocationRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "*")
public class ObjLocationController {

    @Autowired
    private ObjLocationRepository repository;

    @GetMapping
    public List<ObjLocation> getAllCustomers() {
        return repository.findAll();
    }

    // Новый эндпоинт для получения последних N покупателей (например, последних 10)
    @GetMapping("/recent")
    public List<ObjLocation> getRecentCustomers(@RequestParam(defaultValue = "10") int limit) {
        return repository.findTopNByOrderByIdDesc(limit);
    }
}
