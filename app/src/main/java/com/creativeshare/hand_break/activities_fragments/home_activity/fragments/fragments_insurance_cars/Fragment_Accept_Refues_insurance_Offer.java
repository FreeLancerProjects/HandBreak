package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_insurance_cars;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.Notification_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Accept_Refues_insurance_Offer extends Fragment {

    private HomeActivity activity;
    private String current_language;
    private final static String Tag1 = "order";
    private Notification_Model.Data data;

    private Preferences preferences;
    private UserModel userModel;
private ImageView back_arrow;
    public static Fragment_Accept_Refues_insurance_Offer newInstance(Notification_Model.Data data) {
       Fragment_Accept_Refues_insurance_Offer fragment_accept_refues_insurance_offer=new Fragment_Accept_Refues_insurance_Offer();
        Bundle bundle=new Bundle();
        bundle.putSerializable(Tag1,data);
        fragment_accept_refues_insurance_offer.setArguments(bundle);
        return fragment_accept_refues_insurance_offer;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accept_refuse_insurance_offer, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        data= (Notification_Model.Data) getArguments().getSerializable(Tag1);
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        back_arrow=view.findViewById(R.id.arrow);

        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());


        if (current_language.equals("ar")) {
            back_arrow.setRotation(180.0f);
        }


    }



}





