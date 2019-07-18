package com.creativeshare.hand_break.models;

import java.io.Serializable;

public class AppDataModel implements Serializable {

   private String title;
    private String content;

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
