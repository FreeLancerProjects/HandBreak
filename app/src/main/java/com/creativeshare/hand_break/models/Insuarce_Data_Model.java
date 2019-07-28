package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class Insuarce_Data_Model implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public class Data implements Serializable{
        private String request_id;
               private String car_owner_id;
                private String car_owner_phone;
               private String car_owner_name;
                private String insurance_user_id;
                private String insurance_type;
               private String personal_id_num;
                private String personal_id_date;
                private String form_image;
                private String car_image;
                private String car_model;
                private String car_type;
                private String date;
                private String insurance_user_name;
                private double offer_value;

        public String getRequest_id() {
            return request_id;
        }

        public String getCar_owner_id() {
            return car_owner_id;
        }

        public String getCar_owner_phone() {
            return car_owner_phone;
        }

        public String getCar_owner_name() {
            return car_owner_name;
        }

        public String getInsurance_user_id() {
            return insurance_user_id;
        }

        public String getInsurance_type() {
            return insurance_type;
        }

        public String getPersonal_id_num() {
            return personal_id_num;
        }

        public String getPersonal_id_date() {
            return personal_id_date;
        }

        public String getForm_image() {
            return form_image;
        }

        public String getCar_image() {
            return car_image;
        }

        public String getCar_model() {
            return car_model;
        }

        public String getCar_type() {
            return car_type;
        }

        public String getDate() {
            return date;
        }

        public String getInsurance_user_name() {
            return insurance_user_name;
        }

        public double getOffer_value() {
            return offer_value;
        }
    }
}
