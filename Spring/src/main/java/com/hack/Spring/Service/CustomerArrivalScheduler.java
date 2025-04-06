package com.hack.Spring.Service;

import com.hack.Spring.model.ObjLocation;
import com.hack.Spring.repository.ObjLocationRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class CustomerArrivalScheduler {

    private final ObjLocationRepository repository;
    private final Random random = new Random();

    public CustomerArrivalScheduler(ObjLocationRepository repository) {
        this.repository = repository;
    }

    // Каждый 5 секунд добавляем от 1 до 3 новых покупателей
    @Scheduled(fixedDelay = 5000)
    public void addNewCustomers() {
        int count = random.nextInt(3) + 1;  // случайное число от 1 до 3
        for (int i = 0; i < count; i++) {
            // Генерируем координаты для покупателя:
            // Например, x от 5 до 10, y фиксированное значение (например, 15) - это зона ожидания
            double xCord = 5 + random.nextDouble() * 5;
            double yCord = 15;
            ObjLocation newCustomer = new ObjLocation(xCord, yCord);
            repository.save(newCustomer);
            System.out.println("Новый покупатель добавлен: x = " + xCord + ", y = " + yCord);
        }
    }
}
