package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Ads_Adder_Info extends Fragment {
    private AdsActivity adsActivity;
    private String cuurent_language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_adder_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        adsActivity = (AdsActivity) getActivity();
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

    }

    public static Fragment_Ads_Adder_Info newInstance() {
        return new Fragment_Ads_Adder_Info();
    }

}
