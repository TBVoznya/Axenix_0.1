package com.hack.Spring.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "obj_location")
public class ObjLocation {

    @Id
    private Long objId;

    @JsonProperty("xCord")
    private int xCord;
    @JsonProperty("yCord")
    private int yCord;

    public ObjLocation() {}

    public ObjLocation(Long objId, int xCord, int yCord) {
        this.objId = objId;
        this.xCord = xCord;
        this.yCord = yCord;
    }

    public Long getObjId() {
        return objId;
    }

    public void setObjId(Long objId) {
        this.objId = objId;
    }

    public int getXCord() {
        return xCord;
    }

    public void setXCord(int xCord) {
        this.xCord = xCord;
    }

    public int getYCord() {
        return yCord;
    }

    public void setYCord(int yCord) {
        this.yCord = yCord;
    }
}
