package com.creativeshare.hand_break.share;


import android.app.Application;
import android.content.Context;

import com.creativeshare.hand_break.language.Language_Helper;


public class App extends Application {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language_Helper.updateResources(newBase, com.creativeshare.hand_break.preferences.Preferences.getInstance().getLanguage(newBase)));
    }

}

