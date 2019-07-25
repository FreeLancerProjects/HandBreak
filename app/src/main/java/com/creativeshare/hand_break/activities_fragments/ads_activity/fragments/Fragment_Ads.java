package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.adapters.ViewPagerAdapter;
import com.creativeshare.hand_break.models.Adversiment_Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Ads extends Fragment {
    private AdsActivity adsActivity;
    private String cuurent_language;
    private List<Fragment> fragmentList;
    private Fragment_Ads_Detials fragment_ads_detials;
    private Fragment_Ads_Adder_Info fragment_ads_adder_info;
    private ViewPager viewPager;
    private ViewPagerAdapter pageAdapter;
    private ImageView back_arrow;
    private Adversiment_Model adversiment_model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        adsActivity = (AdsActivity) getActivity();
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentList = new ArrayList<>();
        viewPager = view.findViewById(R.id.pager);
        viewPager.beginFakeDrag();
        back_arrow = view.findViewById(R.id.arrow);
        pageAdapter = new ViewPagerAdapter(adsActivity.getSupportFragmentManager());
        intitfragmentspage();
        pageAdapter.AddFragments(fragmentList);
        viewPager.setAdapter(pageAdapter);
        if (cuurent_language.equals("en")) {
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

    public void Back() {
        if (viewPager.getCurrentItem() > 0) {
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);

        } else {
            adsActivity.Back();
        }

    }

    public void gotonext(Adversiment_Model adversiment_model) {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
        this.adversiment_model = adversiment_model;

        fragment_ads_adder_info.setmodel(adversiment_model);
        if (!Adversiment_Model.getId().equals("-1")) {
            fragment_ads_adder_info.setdata();
        }

    }

    public static Fragment_Ads newInstance() {
        return new Fragment_Ads();
    }


}
