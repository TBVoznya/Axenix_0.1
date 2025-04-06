package com.hack.Spring;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.hack.Spring.model.ObjLocation;
import com.hack.Spring.repository.ObjLocationRepository;

@Component
public class DataLoader implements CommandLineRunner {

    private final ObjLocationRepository repository;

    public DataLoader(ObjLocationRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) throws Exception {
        repository.save(new ObjLocation(10, 0));   // касса
        repository.save(new ObjLocation(5, 15));    // покупатель
        repository.save(new ObjLocation(6, 15));
        repository.save(new ObjLocation(7, 15));
        repository.save(new ObjLocation(8, 15));
        // Добавь другие данные при необходимости
    }
}
