package com.hack.Spring.model;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;

@Entity
@Table(name = "obj_location")
public class ObjLocation {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long objId;
    
    private double xCord;
    private double yCord;

    public ObjLocation() {}

    public ObjLocation(double xCord, double yCord) {
        this.xCord = xCord;
        this.yCord = yCord;
    }

    // геттеры и сеттеры
    public Long getObjId() { return objId; }
    public void setObjId(Long objId) { this.objId = objId; }

    public double getXCord() { return xCord; }
    public void setXCord(double xCord) { this.xCord = xCord; }

    public double getYCord() { return yCord; }
    public void setYCord(double yCord) { this.yCord = yCord; }
}
