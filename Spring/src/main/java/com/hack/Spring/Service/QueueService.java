package com.hack.Spring.Service;

import com.hack.Spring.dto.QueueInfoDto;
import com.hack.Spring.model.ObjLocation;
import com.hack.Spring.repository.ObjLocationRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueueService {

    private final ObjLocationRepository locationRepository;

    public QueueService(ObjLocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<QueueInfoDto> simulateQueues() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'simulateQueues'");
    }

    /*public List<QueueInfoDto> simulateQueues() {
        List<ObjLocation> allObjects = locationRepository.findAll();

        // Предположим, что кассы — это те, у кого y == 0, а покупатели — y > 0
        List<ObjLocation> cashiers = new ArrayList<>();
        List<ObjLocation> customers = new ArrayList<>();

        for (ObjLocation obj : allObjects) {
            if (obj.getYCord() == 0) {
                cashiers.add(obj);
            } else {
                customers.add(obj);
            }
        }

        List<QueueInfoDto> queues = new ArrayList<>();

        for (ObjLocation cashier : cashiers) {
            List<ObjLocation> queue = new ArrayList<>();
            for (ObjLocation customer : customers) {
                double dx = cashier.getXCord() - customer.getXCord();
                double dy = cashier.getYCord() - customer.getYCord();
                double distance = Math.sqrt(dx * dx + dy * dy);
                if (distance <= 5.0) { // считаем что он в очереди, если рядом с кассой
                    queue.add(customer);
                }
            }

            QueueInfoDto dto = new QueueInfoDto();
            dto.setCashierId(cashier.getObjId());
            dto.setQueueSize(queue.size());
            dto.setAvgServiceTime(queue.size() > 5 ? 15.0 : 8.0); // время больше при большой очереди
    
            queues.add(dto);
        }

        return queues;
    } */
}
