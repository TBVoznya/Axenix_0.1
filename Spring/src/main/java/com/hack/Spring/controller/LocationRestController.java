/*package com.hack.Spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hack.Spring.model.ObjLocation;
import com.hack.Spring.repository.ObjLocationRepository;

@RestController
@RequestMapping("/api/locations")
public class LocationRestController {
    @Autowired
    private ObjLocationRepository repository;

    @GetMapping
    public List<ObjLocation> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ObjLocation create(@RequestBody ObjLocation location) {
        return repository.save(location);
    }
}
*/