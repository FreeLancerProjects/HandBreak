package com.creativeshare.hand_break.activities_fragments.ads_activity.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads_Adder_Info;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads_Detials;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Adviersiment_Terms_Conditions;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Home;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Search;
import com.creativeshare.hand_break.adapters.ViewPagerAdapter;
import com.creativeshare.hand_break.language.Language_Helper;
import com.creativeshare.hand_break.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AdsActivity extends AppCompatActivity {

    private String cuurent_language;
    private FragmentManager fragmentManager;
    private int fragment_count=0;
    private Fragment_Ads fragment_ads;
    private Fragment_Adviersiment_Terms_Conditions fragment_adviersiment_terms_conditions;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language_Helper.updateResources(newBase,  Preferences.getInstance().getLanguage(newBase)));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        initView();
        if(savedInstanceState==null){
            DisplayFragmentAds();
        }


    }
    private void initView() {
        fragmentManager = this.getSupportFragmentManager();
        Paper.init(this);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }
    public void DisplayFragmentAds()
    {
        fragment_count+=1;
        if (fragment_ads == null) {
            fragment_ads = Fragment_Ads.newInstance();
        }

        if (fragment_ads.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_ads).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_ads, "fragment_ads").addToBackStack("fragment_ads").commit();

        }

    }

    public void DisplayFragmentterms()
    {
        fragment_count+=1;
        if (fragment_adviersiment_terms_conditions == null) {
            fragment_adviersiment_terms_conditions = Fragment_Adviersiment_Terms_Conditions.newInstance();
        }

        if (fragment_adviersiment_terms_conditions.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_adviersiment_terms_conditions).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_adviersiment_terms_conditions, "fragment_adviersiment_terms_conditions").addToBackStack("fragment_adviersiment_terms_conditions").commit();

        }

    }


    public void gotonext() {
if(fragment_ads!=null&&fragment_ads.isAdded()){
    fragment_ads.gotonext();
}
    }
    public void onBackPressed() {
        Back();    }

    public void Back() {
       if(fragment_count>1){
fragment_count-=1;
super.onBackPressed();
       }
       else {
           finish();
       }

    }
}
