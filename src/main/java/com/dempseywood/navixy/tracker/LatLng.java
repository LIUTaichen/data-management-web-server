package com.dempseywood.navixy.tracker;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class LatLng {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    public  Double latitude;
    public Double longitude;

    public LatLng(){

    }

    public LatLng(Double lat, Double lng){
        this.latitude = lat;
        this.longitude = lng;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
