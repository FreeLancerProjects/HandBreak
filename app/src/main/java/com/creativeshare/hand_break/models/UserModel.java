package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private String user_id = "";
    private String user_name;
    private String user_email;
    private String user_phone_code;
    private String user_phone;
    private String user_full_nam;
    private String user_image;
    private String user_address;
    private String commercial_register;
    private String user_type;
    private String user_google_lat;
    private String user_google_long;
    private String user_country;
    private String user_city;
    private boolean user_follow;
    private boolean rating_status;
    private float rating_value;
    private String ar_city_title;
    private String en_city_title;
private String insurance_services;
    private List<UserModel.Advertsing> advertsing;

    public String getUser_id() {
        return user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public String getUser_phone_code() {
        return user_phone_code;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public String getUser_full_nam() {
        return user_full_nam;
    }

    public String getUser_image() {
        return user_image;
    }

    public String getUser_address() {
        return user_address;
    }

    public String getCommercial_register() {
        return commercial_register;
    }

    public String getUser_type() {
        return user_type;
    }

    public String getUser_google_lat() {
        return user_google_lat;
    }

    public String getUser_google_long() {
        return user_google_long;
    }

    public String getUser_country() {
        return user_country;
    }

    public String getUser_city() {
        return user_city;
    }

    public boolean isUser_follow() {
        return user_follow;
    }

    public void setUser_follow(boolean user_follow) {
        this.user_follow = user_follow;
    }

    public boolean isRating_status() {
        return rating_status;
    }

    public float getRating_value() {
        return rating_value;
    }

    public void setRating_status(boolean rating_status) {
        this.rating_status = rating_status;
    }

    public void setRating_value(float rating_value) {
        this.rating_value = rating_value;
    }

    public String getAr_city_title() {
        return ar_city_title;
    }

    public String getEn_city_title() {
        return en_city_title;
    }

    public String getInsurance_services() {
        return insurance_services;
    }
    public List<UserModel.Advertsing> getAdvertsing() {
        return advertsing;
    }
    public class  Advertsing  implements Serializable {
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
        private String main_category_title;

        public String getMain_category_title() {
            return main_category_title;
        }

        private List<Catogry_Model.Advertsing.Advertisement_images> advertisement_images;
        private List<Catogry_Model.Advertsing.Same_advertisements> same_advertisements;
        private boolean follow_status;
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

        public List<Catogry_Model.Advertsing.Advertisement_images> getAdvertisement_images() {
            return advertisement_images;
        }

        public List<Catogry_Model.Advertsing.Same_advertisements> getSame_advertisements() {
            return same_advertisements;
        }

        public boolean isFollow_status() {
            return follow_status;
        }

        public void setFollow_status(boolean follow_status) {
            this.follow_status = follow_status;
        }
    }
}
