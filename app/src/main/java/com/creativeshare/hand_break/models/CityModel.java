package com.creativeshare.hand_break.models;

import java.io.Serializable;

public class CityModel implements Serializable {

    private String id_city;
    private String city_title;


    public CityModel(String city_title) {
        this.city_title = city_title;
    }

    public String getId_city() {
        return id_city;
    }

    public String getCity_title() {
        return city_title;
    }
}
