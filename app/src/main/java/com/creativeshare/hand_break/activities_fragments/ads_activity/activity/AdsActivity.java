package com.creativeshare.hand_break.activities_fragments.ads_activity.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads_Adder_Info;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads_Detials;
import com.creativeshare.hand_break.adapters.ViewPagerAdapter;
import com.creativeshare.hand_break.language.Language;
import com.creativeshare.hand_break.language.Language_Helper;
import com.creativeshare.hand_break.preferences.Preferences;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class AdsActivity extends AppCompatActivity {

    private String cuurent_language;
    private List<Fragment> fragmentList;
    private Fragment_Ads_Detials fragment_ads_detials;
    private Fragment_Ads_Adder_Info fragment_ads_adder_info;
    private ViewPager viewPager;
    private ViewPagerAdapter pageAdapter;
private ImageView back_arrow;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language_Helper.updateResources(newBase,  Preferences.getInstance().getLanguage(newBase)));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        initView();


    }
    private void initView() {
        Paper.init(this);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentList = new ArrayList<>();
        viewPager = findViewById(R.id.pager);
        back_arrow=findViewById(R.id.arrow);
        pageAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        intitfragmentspage();
        pageAdapter.AddFragments(fragmentList);
        viewPager.setAdapter(pageAdapter);
        if(cuurent_language.equals("en")){
            back_arrow.setRotation(180.0f);
        }
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Back();
            }
        });
    }


    private void intitfragmentspage() {
        fragment_ads_detials = Fragment_Ads_Detials.newInstance();
        fragment_ads_adder_info = Fragment_Ads_Adder_Info.newInstance();

        fragmentList.add(fragment_ads_detials);
        fragmentList.add(fragment_ads_adder_info);

    }

    public void gotonext() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);

    }
    public void onBackPressed() {
        Back();    }

    public void Back() {
       if(viewPager.getCurrentItem()>0){
           viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

       }
       else {
           finish();
       }

    }
}
