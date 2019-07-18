package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.ViewPagerAdapter;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Ads_Detials extends Fragment {
    private AdsActivity adsActivity;
    private String cuurent_language;
private LinearLayout ll_continue;
private  ImageView bt_arrow;
private TextView tv_terms;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_detials, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        adsActivity = (AdsActivity) getActivity();
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        ll_continue=view.findViewById(R.id.ll_continue);
        bt_arrow=view.findViewById(R.id.bt_arrow);
        tv_terms=view.findViewById(R.id.tv_terms);
        if(cuurent_language.equals("en")){
            bt_arrow.setRotation(180.0f);
        }
        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adsActivity.DisplayFragmentterms();
            }
        });
        ll_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
adsActivity.gotonext();
            }
        });

         }

    public static Fragment_Ads_Detials newInstance() {
        return new Fragment_Ads_Detials();
    }

}
