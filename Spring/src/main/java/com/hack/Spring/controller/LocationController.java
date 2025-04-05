/*package com.hack.Spring.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.hack.Spring.model.ObjLocation;
import com.hack.Spring.repository.ObjLocationRepository;

@Controller
public class LocationController {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ObjLocationRepository locationRepository;

    @MessageMapping("/updateLocation")
    public void updateLocation(ObjLocation location) {
        locationRepository.save(location);
        messagingTemplate.convertAndSend("/topic/locations", location);
    }
}
*/