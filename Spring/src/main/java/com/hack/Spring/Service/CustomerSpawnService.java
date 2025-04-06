package com.hack.Spring.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hack.Spring.model.ObjLocation;
import com.hack.Spring.repository.ObjLocationRepository;

import java.util.Random;

@Service
public class CustomerSpawnService {

    @Autowired
    private ObjLocationRepository repository;

    private final Random random = new Random();

    @Scheduled(fixedRate = 5000)
    public void spawnCustomer() {
        // Например, координаты в зоне входа или случайно в зале
        double x = -8 + random.nextDouble() * 3;  // Пример входа
        double y = 6 + random.nextDouble() * 5;   // Пример кассы

        ObjLocation newCustomer = new ObjLocation(x, y);
        repository.save(newCustomer);
    }
}
