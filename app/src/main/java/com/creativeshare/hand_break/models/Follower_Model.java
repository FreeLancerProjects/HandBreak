package com.creativeshare.hand_break.models;

import java.io.Serializable;
import java.util.List;

public class Follower_Model  implements Serializable {
    private List<Data> data;

    public List<Data> getData() {
        return data;
    }

    public  class Data implements Serializable
    {
        private String user_name;
            private String user_image;

        public String getUser_name() {
            return user_name;
        }

        public String getUser_image() {
            return user_image;
        }
    }
}
