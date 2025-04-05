package com.hack.Spring.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hack.Spring.dto.ShelfDto;
import com.hack.Spring.dto.StoreDto;

@RestController
@RequestMapping("/api")
public class StoreController {

    @GetMapping("/store")
    public StoreDto getStore() {
        StoreDto store = new StoreDto();
        store.setName("Магазин №1");
        store.setWidth(10);
        store.setHeight(10);

        List<ShelfDto> shelves = new ArrayList<>();
        shelves.add(new ShelfDto(1, 2, "Электроника", 50));
        shelves.add(new ShelfDto(3, 4, "Продукты", 100));
        shelves.add(new ShelfDto(5, 6, "Бытовая химия", 80));

        store.setShelves(shelves);

        return store;
    }
}
