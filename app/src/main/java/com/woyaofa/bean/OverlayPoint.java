package com.woyaofa.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by LoaR on 15/11/10.
 */
public class OverlayPoint implements Serializable {
    private int id;
    private String name;
    private double lng;
    private double lat;

    public OverlayPoint(int id, String name, double lng, double lat) {
        this.id = id;
        this.name = name;
        this.lng = lng;
        this.lat = lat;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}
