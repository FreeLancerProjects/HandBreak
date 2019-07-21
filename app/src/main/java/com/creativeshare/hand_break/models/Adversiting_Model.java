package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class Adversiting_Model
    implements Serializable {
        private String id_advertisement;
        private String advertisement_user;
        private String is_sold;
        private String advertisement_code;
        private String main_category_fk;
        private String sub_category_fk;
        private String model_id_fk;
        private String advertisement_title;
        private String advertisement_content;
        private String advertisement_price;
        private String advertisement_type;
        private String advertisement_date;
        private String city;
        private String google_lat;
        private String google_long;
        private String phone;
        private String show_phone;
        private String view_count;
        private String share_count;
        private String share_link;
        private String publisher;
        private String approved;
        private String available;
        private String user_name;
        private String city_title;
        private String main_image;
        private List<Advertisement_images> advertisement_images;
        private List<Same_advertisements> same_advertisements;

        public class Advertisement_images implements Serializable {
            private String image_id;
            private String image_name;

            public String getImage_id() {
                return image_id;
            }

            public String getImage_name() {
                return image_name;
            }
        }

        public class Same_advertisements implements Serializable {
            private String id_advertisement;

            public String getId_advertisement() {
                return id_advertisement;
            }
        }

        public String getId_advertisement() {
            return id_advertisement;
        }

        public String getAdvertisement_user() {
            return advertisement_user;
        }

        public String getIs_sold() {
            return is_sold;
        }

        public String getAdvertisement_code() {
            return advertisement_code;
        }

        public String getMain_category_fk() {
            return main_category_fk;
        }

        public String getSub_category_fk() {
            return sub_category_fk;
        }

        public String getModel_id_fk() {
            return model_id_fk;
        }

        public String getAdvertisement_title() {
            return advertisement_title;
        }

        public String getAdvertisement_content() {
            return advertisement_content;
        }

        public String getAdvertisement_price() {
            return advertisement_price;
        }

        public String getAdvertisement_type() {
            return advertisement_type;
        }

        public String getAdvertisement_date() {
            return advertisement_date;
        }

        public String getCity() {
            return city;
        }

        public String getGoogle_lat() {
            return google_lat;
        }

        public String getGoogle_long() {
            return google_long;
        }

        public String getPhone() {
            return phone;
        }

        public String getShow_phone() {
            return show_phone;
        }

        public String getView_count() {
            return view_count;
        }

        public String getShare_count() {
            return share_count;
        }

        public String getShare_link() {
            return share_link;
        }

        public String getPublisher() {
            return publisher;
        }

        public String getApproved() {
            return approved;
        }

        public String getAvailable() {
            return available;
        }

        public String getUser_name() {
            return user_name;
        }

        public String getCity_title() {
            return city_title;
        }

        public String getMain_image() {
            return main_image;
        }

        public List<Advertisement_images> getAdvertisement_images() {
            return advertisement_images;
        }

        public List<Same_advertisements> getSame_advertisements() {
            return same_advertisements;
        }
    }

