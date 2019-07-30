package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class Notification_Model implements Serializable {
    private List<Data> data;
    private Meta meta;

    public List<Data> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class Data implements Serializable {

        private String id_notification;
        private String id_advertisement;
        private String title_notification;
        private String from_user_id_fk;
        private String to_user_id_fk;
        private String action_type;
        private String content_notification;
        private String date_notification;
        private String from_user_name;
        private String from_user_phone_code;
        private String from_user_phone;
        private String from_user_image;
        private String request_id;
        private String offer_value;

        public String getId_notification() {
            return id_notification;
        }

        public String getId_advertisement() {
            return id_advertisement;
        }

        public String getTitle_notification() {
            return title_notification;
        }

        public String getFrom_user_id_fk() {
            return from_user_id_fk;
        }

        public String getTo_user_id_fk() {
            return to_user_id_fk;
        }

        public String getAction_type() {
            return action_type;
        }

        public String getContent_notification() {
            return content_notification;
        }

        public String getDate_notification() {
            return date_notification;
        }

        public String getFrom_user_name() {
            return from_user_name;
        }

        public String getFrom_user_phone_code() {
            return from_user_phone_code;
        }

        public String getFrom_user_phone() {
            return from_user_phone;
        }

        public String getFrom_user_image() {
            return from_user_image;
        }

        public String getRequest_id() {
            return request_id;
        }

        public String getOffer_value() {
            return offer_value;
        }
    }

    public class Meta implements Serializable {
        private int current_page;
        private int last_page;
        private int total_notification;

        public int getCurrent_page() {
            return current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public int getTotal_notification() {
            return total_notification;
        }
    }
}