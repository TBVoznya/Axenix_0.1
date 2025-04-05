package com.hack.Spring.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import jakarta.persistence.*;

@Entity
@Table(name = "obj_location")
public class ObjLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long objId;

    public Long getObjId() {
        return objId;
    }
    public void setObjId(Long objId) {
        this.objId = objId;
    }
    private double xCord;

    public double getxCord() {
        return xCord;
    }
    public void setxCord(double xCord) {
        this.xCord = xCord;
    }
    private double yCord;

    public double getyCord() {
        return yCord;
    }
    public void setyCord(double yCord) {
        this.yCord = yCord;
    }
    public int getYCord() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getYCord'");
    }
    public int getXCord() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getXCord'");
    }

}
