package com.hack.Spring.controller;

import com.hack.Spring.Service.QueueService;
import com.hack.Spring.dto.QueueInfoDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class QueueController {

    private final QueueService queueService;

    public QueueController(QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping("/api/queues")
    public List<QueueInfoDto> getQueues() {
        return queueService.simulateQueues();
    }
}
