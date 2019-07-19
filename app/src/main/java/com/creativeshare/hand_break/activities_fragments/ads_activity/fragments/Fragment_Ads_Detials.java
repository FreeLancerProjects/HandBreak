package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Spinner_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_Sub_Sub_catogry_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_Sub_catogry_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_catogry_Adapter;
import com.creativeshare.hand_break.adapters.ViewPagerAdapter;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Ads_Detials extends Fragment {
    private AdsActivity adsActivity;
    private String cuurent_language;
private LinearLayout ll_continue;
private  ImageView bt_arrow;
private TextView tv_terms;
    private List<Catogry_Model.Categories.sub> subs;
    private List<Catogry_Model.Categories> categories;
    private List<Catogry_Model.Categories.sub.Sub> subs_sub;

    private Spinner_Sub_catogry_Adapter spinner_sub_catogry_adapter;
    private Spinner_catogry_Adapter spinner_catogry_adapter;
    private Spinner_Sub_Sub_catogry_Adapter spinner_sub_sub_catogry_adapter;
    private Spinner sp_cat,sp_sub_cat,sp_model, cities;
    private Spinner_Adapter city_adapter;
    private List<CityModel> cities_models;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_detials, container, false);
        initView(view);
getCities();
categories();
        return view;
    }

    private void initView(View view) {
        adsActivity = (AdsActivity) getActivity();
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        ll_continue=view.findViewById(R.id.ll_continue);
        bt_arrow=view.findViewById(R.id.bt_arrow);
        tv_terms=view.findViewById(R.id.tv_terms);
        sp_cat=view.findViewById(R.id.sp_cat);
        sp_sub_cat=view.findViewById(R.id.sp_sub);
        sp_model = view.findViewById(R.id.sp_model);
        cities_models = new ArrayList<>();
        categories=new ArrayList<>();
        subs = new ArrayList<>();
        subs_sub=new ArrayList<>();

        if (cuurent_language.equals("ar")) {
            cities_models.add(new CityModel("إختر"));
            subs.add(new Catogry_Model.Categories.sub("إختر"));
            categories.add(new Catogry_Model.Categories("إختر"));
            subs_sub.add(new Catogry_Model.Categories.sub.Sub("إختر"));

        } else {
            cities_models.add(new CityModel("Choose"));
            subs.add(new Catogry_Model.Categories.sub("Choose"));
            categories.add(new Catogry_Model.Categories("Choose"));
            subs_sub.add(new Catogry_Model.Categories.sub.Sub("Choose"));

        }


        cities = view.findViewById(R.id.sp_city);
        spinner_catogry_adapter=new Spinner_catogry_Adapter(adsActivity,categories);
        spinner_sub_catogry_adapter = new Spinner_Sub_catogry_Adapter(adsActivity, subs);
        spinner_sub_sub_catogry_adapter=new Spinner_Sub_Sub_catogry_Adapter(adsActivity,subs_sub);
        sp_cat.setAdapter(spinner_catogry_adapter);
        sp_sub_cat.setAdapter(spinner_sub_catogry_adapter);
        sp_model.setAdapter(spinner_sub_sub_catogry_adapter);
        city_adapter = new Spinner_Adapter(adsActivity, cities_models);
        cities.setAdapter(city_adapter);
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
sp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        subs.clear();
        if (cuurent_language.equals("ar")) {
            subs.add(new Catogry_Model.Categories.sub("إختر"));

        } else {
            subs.add(new Catogry_Model.Categories.sub("Choose"));

        }
        if(i>0&&categories.get(i).getsub()!=null){
        subs.addAll(categories.get(i).getsub());
        spinner_sub_catogry_adapter.notifyDataSetChanged();}
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
sp_sub_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        subs_sub.clear();
        if (cuurent_language.equals("ar")) {
            subs_sub.add(new Catogry_Model.Categories.sub.Sub("إختر"));

        } else {
            subs_sub.add(new Catogry_Model.Categories.sub.Sub("Choose"));

        }
        if(i>0&&subs.get(i).getSubs()!=null){
        subs_sub.addAll(subs.get(i).getSubs());
        spinner_sub_sub_catogry_adapter.notifyDataSetChanged();}
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
         }

    public static Fragment_Ads_Detials newInstance() {
        return new Fragment_Ads_Detials();
    }
    private void getCities() {

        final ProgressDialog dialog = Common.createProgressDialog(adsActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService()
                .getCities(cuurent_language)
                .enqueue(new Callback<List<CityModel>>() {
                    @Override
                    public void onResponse(Call<List<CityModel>> call, Response<List<CityModel>> response) {
                        dialog.dismiss();

                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                cities_models.clear();
                                if (cuurent_language.equals("ar")) {
                                    cities_models.add(new CityModel("إختر"));
                                } else {
                                    cities_models.add(new CityModel("Choose"));

                                }
                                cities_models.addAll(response.body());
                                city_adapter.notifyDataSetChanged();
                            }
                        } else {
                            try {
                                Toast.makeText(adsActivity, R.string.failed, Toast.LENGTH_SHORT).show();
                                Log.e("Error_code", response.code() + "" + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CityModel>> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(adsActivity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }
    public void categories() {

        Api.getService().getcateogries(cuurent_language).enqueue(new Callback<Catogry_Model>() {
            @Override
            public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                //progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body().getCategories() != null && response.body().getCategories().size() > 0) {
                        categories.addAll(response.body().getCategories());
                        spinner_catogry_adapter.notifyDataSetChanged();
                        //setsub();
                    } else {
                        // error.setText(activity.getString(R.string.no_data));
                        //recc.setVisibility(View.GONE);
                        //      mPager.setVisibility(View.GONE);
                    }

                    // Inflate the layout for this fragment
                } else if (response.code() == 404) {
                    //error.setText(activity.getString(R.string.no_data));
                    //recc.setVisibility(View.GONE);
                    //mPager.setVisibility(View.GONE);
                } else {
                    //recc.setVisibility(View.GONE);
                    //mPager.setVisibility(View.GONE);
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //error.setText(activity.getString(R.string.faild));

                }
            }

            @Override
            public void onFailure(Call<Catogry_Model> call, Throwable t) {

                Log.e("Error", t.getMessage());
                //progBar.setVisibility(View.GONE);
                //error.setText(activity.getString(R.string.faild));
                //mPager.setVisibility(View.GONE);
            }
        });
    }
}
