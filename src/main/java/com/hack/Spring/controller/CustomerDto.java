package com.hack.Spring.controller;

public class CustomerDto {
    private String id;
    private String name;
    private int age;
    private String profile; // поведенческий профиль
    private double x;
    private double y;
    private double z;

    // Конструктор
    public CustomerDto(String id, String name, int age, String profile) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.profile = profile;
    }

    public CustomerDto() {
        //TODO Auto-generated constructor stub
    }

    // Геттеры и сеттеры
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    public double getX() { 
        return x;
    }
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y; 
    }
    public void setY(double y) { 
        this.y = y; 
    }

    public double getZ() { 
        return z;
    }
    public void setZ(double z) { 
        this.z = z; 
    }
}

