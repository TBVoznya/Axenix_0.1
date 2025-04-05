package com.hack.Spring.Service;

import org.springframework.stereotype.Service;

import com.hack.Spring.model.Customer;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {
    private final List<Customer> customers = new ArrayList<>();
    private final Random random = new Random();
    private final AtomicLong counter = new AtomicLong();
    private final int maxQueueLength = 5;
    private final double startX = -5;
    private final double stepX = 2.0;

    public CustomerService() {
        for (int i = 0; i < 3; i++) {
            customers.add(generateCustomer(i));
        }
    }

    private Customer generateCustomer(int queueNumber) {
        long id = counter.incrementAndGet();
        double x = startX + stepX * queueNumber;
        return new Customer(id, x, 0, 25, queueNumber, true);
    }

    public List<Customer> getAllCustomers() {
        updateQueue(); // эмулируем движение очереди
        return customers;
    }

    private void updateQueue() {
        Iterator<Customer> iterator = customers.iterator();
        while (iterator.hasNext()) {
            Customer c = iterator.next();
            if (random.nextDouble() < 0.01) {
                iterator.remove(); // клиент обслужен
            }
        }

        int currentSize = customers.size();
        if (currentSize < maxQueueLength + 2) {
            // добавляем новых
            int needed = maxQueueLength + 2 - currentSize;
            for (int i = 0; i < needed; i++) {
                customers.add(generateCustomer(currentSize + i));
            }
        }

        // обновляем координаты (очередь выстраивается)
        for (int i = 0; i < customers.size(); i++) {
            Customer c = customers.get(i);
            c.setX(startX + i * stepX);
            c.setZ(25); // у кассы
            c.setQueueNumber(i);
        }
    }
}
