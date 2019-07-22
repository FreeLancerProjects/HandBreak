package com.creativeshare.hand_break.models;

import java.io.Serializable;

public class ChatUserModel implements Serializable {

    private String name;
    private String image;
    private String id;
    private String room_id;
    private String registration_date;


    public ChatUserModel(String name, String image, String id, String room_id, String registration_date) {
        this.name = name;
        this.image = image;
        this.id = id;
        this.room_id = room_id;
        this.registration_date = registration_date;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getId() {
        return id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public String getRegistration_date() {
        return registration_date;
    }
}
