package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class UserSearchDataModel implements Serializable {

    private List<UserSearchModel> data;

    public List<UserSearchModel> getData() {
        return data;
    }

    public class UserSearchModel implements Serializable
    {
        private String user_id;
        private String user_name;
        private String user_image;
        private String date_registration;
        private String room_id;

        public String getUser_id() {
            return user_id;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public String getDate_registration() {
            return date_registration;
        }

        public String getRoom_id() {
            return room_id;
        }

        public void setRoom_id(String room_id) {
            this.room_id = room_id;
        }
    }
}
