package com.hack.Spring.dto;

public class QueueInfoDto {
    private Long cashierId;
    public Long getCashierId() {
        return cashierId;
    }
    public void setCashierId(Long cashierId) {
        this.cashierId = cashierId;
    }
    private int queueSize;
    public int getQueueSize() {
        return queueSize;
    }
    public void setQueueSize(int queueSize) {
        this.queueSize = queueSize;
    }
    private double avgServiceTime; // в секундах
    public double getAvgServiceTime() {
        return avgServiceTime;
    }
    public void setAvgServiceTime(double avgServiceTime) {
        this.avgServiceTime = avgServiceTime;
    }

}

