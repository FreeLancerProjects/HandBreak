package com.creativeshare.hand_break.models;

import android.net.Uri;

import java.io.Serializable;
import java.util.List;

public class Adversiment_Comment_Model implements Serializable {
    private List<Data> data;
    private Meta meta;

    public List<Data> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class Data implements Serializable
    {
        private String advertisement_id_fk;
            private String comment_text;
            private String date;
            private String user_name;

        public String getAdvertisement_id_fk() {
            return advertisement_id_fk;
        }

        public String getComment_text() {
            return comment_text;
        }

        public String getDate() {
            return date;
        }

        public String getUser_name() {
            return user_name;
        }
    }
    public class Meta implements Serializable{
        private int current_page;
        private int last_page;
        private int total;

        public int getCurrent_page() {
            return current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public int getTotal() {
            return total;
        }
    }
}
