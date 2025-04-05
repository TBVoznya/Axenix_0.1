package com.hack.Spring.model;

public class Customer {
    private Long id;
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private double x;
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    private double y;
    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    private double z;
    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }

    private int queueNumber;
    public int getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(int queueNumber) {
        this.queueNumber = queueNumber;
    }

    private boolean inQueue;

    public boolean isInQueue() {
        return inQueue;
    }

    public void setInQueue(boolean inQueue) {
        this.inQueue = inQueue;
    }

    public Customer(Long id, double x, double y, double z, int queueNumber, boolean inQueue) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.z = z;
        this.queueNumber = queueNumber;
        this.inQueue = inQueue;
    }
}
