package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class MessageDataModel implements Serializable {

    private List<MessageModel> data;
    private Meta meta;
    private RoomData room_data;


    public List<MessageModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public RoomData getRoom_data() {
        return room_data;
    }

    public class Meta implements Serializable
    {
        private int current_page;

        public int getCurrent_page() {
            return current_page;
        }
    }

    public class RoomData implements Serializable
    {
        private String room_id;
        private String date_registration;
        private String create_room_at;
        private String second_user;
        private String second_user_name;
        private String second_user_image;

        public String getRoom_id() {
            return room_id;
        }

        public String getDate_registration() {
            return date_registration;
        }

        public String getCreate_room_at() {
            return create_room_at;
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
    }
}
