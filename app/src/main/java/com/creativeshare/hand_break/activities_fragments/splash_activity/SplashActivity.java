package com.creativeshare.hand_break.activities_fragments.splash_activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.activities_fragments.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.hand_break.language.Language_Helper;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.tags.Tags;

public class SplashActivity extends AppCompatActivity {

    private FrameLayout fl;
    private Preferences preferences;
    private String session;
    private Animation animation;

    @Override
    protected void attachBaseContext(Context base)
    {
        super.attachBaseContext(Language_Helper.updateResources(base, Preferences.getInstance().getLanguage(base)));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
    }

    private void initView() {
        preferences=Preferences.getInstance();
        session=preferences.getSession(this);
        fl=findViewById(R.id.fl);

        animation= AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade);

        fl.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(session.equals(Tags.session_login)){
                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
                else {
                    Intent intent = new Intent(SplashActivity.this, Login_Activity.class);
                    startActivity(intent);
                }
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
