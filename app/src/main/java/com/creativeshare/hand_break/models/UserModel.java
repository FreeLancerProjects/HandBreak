package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class UserModel implements Serializable {

    private int id;
    private String token;
    private String name;
    private String email;
    private String avatar;

    private String phone;
    private String fire_base_token;
    private double balance;
    private List<User_Cvs> user_cvs;

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getPhone() {
        return phone;
    }

    public String getFire_base_token() {
        return fire_base_token;
    }

    public double getBalance() {
        return balance;
    }


    public List<User_Cvs> getUser_cvs() {
        return user_cvs;
    }

    public class User_Cvs implements Serializable{
        private int id;
        private String cv_image;
        private int user_id_fk;

        public int getId() {
            return id;
        }

        public String getcv_image() {
            return cv_image;
        }

        public int getUser_id_fk() {
            return user_id_fk;
        }
    }
}
