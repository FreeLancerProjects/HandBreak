package com.creativeshare.hand_break.share;


import android.app.Application;
import android.content.Context;

import com.creativeshare.hand_break.language.Language;


public class App extends Application {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase, com.creativeshare.sunfun.preferences.Preferences.getInstance().getLanguage(newBase)));
    }

}

