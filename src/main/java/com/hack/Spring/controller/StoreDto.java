package com.hack.Spring.controller;

import java.util.List;

public class StoreDto {
    private String name;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    private int width;
    public int getWidth() {
        return width;
    }
    public void setWidth(int width) {
        this.width = width;
    }
    private int height;
    public int getHeight() {
        return height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    private List<ShelfDto> shelves;
    public List<ShelfDto> getShelves() {
        return shelves;
    }
    public void setShelves(List<ShelfDto> shelves) {
        this.shelves = shelves;
    }

}
