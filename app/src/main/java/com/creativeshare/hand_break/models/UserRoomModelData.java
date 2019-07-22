package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class UserRoomModelData implements Serializable {

    private List<UserRoomModel> data;
    private Meta meta;

    public List<UserRoomModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class UserRoomModel implements Serializable
    {
        private String room_id;
        private String second_user;
        private String second_user_name;
        private String second_user_image;
        private String date_registration;
        private String message_text;
        private String message_date;

        public String getRoom_id() {
            return room_id;
        }

        public String getSecond_user() {
            return second_user;
        }

        public String getSecond_user_name() {
            return second_user_name;
        }

        public String getSecond_user_image() {
            return second_user_image;
        }

        public String getDate_registration() {
            return date_registration;
        }

        public String getMessage_text() {
            return message_text;
        }

        public String getMessage_date() {
            return message_date;
        }
    }

    public class Meta
    {
        private int current_page;

        public int getCurrent_page() {
            return current_page;
        }
    }
}
