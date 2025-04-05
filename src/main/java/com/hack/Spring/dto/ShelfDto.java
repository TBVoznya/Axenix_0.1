package com.hack.Spring.dto;

public class ShelfDto {
    private int x;
    public ShelfDto(int i, int j, String string, int k) {
        //TODO Auto-generated constructor stub
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    private int y;
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    private String type;     // например, "электроника", "продукты"
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    private int capacity;    // вместимость
    public int getCapacity() {
        return capacity;
    }
    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
