package com.creativeshare.hand_break.share;


import android.app.Application;
import android.content.Context;

import com.creativeshare.hand_break.language.Language_Helper;


public class App extends Application {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language_Helper.updateResources(newBase, com.creativeshare.hand_break.preferences.Preferences.getInstance().getLanguage(newBase)));
    }
    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceUtil.overrideFont(this, "SERIF", "font/din-next-lt-w23-regular.ttf"); // font from assets: "assets/fonts/Roboto-Regular.ttf
        TypefaceUtil.overrideFont(this, "DEFAULT", "font/din-next-lt-w23-regular.ttf");
        TypefaceUtil.overrideFont(this, "MONOSPACE", "font/din-next-lt-w23-regular.ttf");
        TypefaceUtil.overrideFont(this, "SANS_SERIF", "font/din-next-lt-w23-regular.ttf");
    }

}

