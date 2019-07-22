package com.creativeshare.hand_break.activities_fragments.home_activity.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.CatogriesAdapter;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Home extends Fragment {
    private HomeActivity homeActivity;
    private AHBottomNavigation ah_bottom_nav;
    private String cuurent_language;
    private FloatingActionButton fab_add_ads;
    private RecyclerView rec_catogry;
    private CatogriesAdapter catogriesAdapter;
    private List<Catogry_Model.Categories> categories;
private Preferences preferences;
private UserModel userModel;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        categories = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        ah_bottom_nav = view.findViewById(R.id.ah_bottom_nav);
        fab_add_ads = view.findViewById(R.id.fab_add_ads);
        rec_catogry = view.findViewById(R.id.rec_data);
        catogriesAdapter = new CatogriesAdapter(categories, homeActivity, this);
        rec_catogry.setDrawingCacheEnabled(true);
        rec_catogry.setItemViewCacheSize(25);
        rec_catogry.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_catogry.setLayoutManager(new LinearLayoutManager(homeActivity, RecyclerView.HORIZONTAL, false));
        rec_catogry.setAdapter(catogriesAdapter);
        fab_add_ads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel!=null){
                    homeActivity.getoAds("-1");
                }
                else {
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                }
            }
        });
        setUpBottomNavigation();
        ah_bottom_nav.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                switch (position) {
                    case 0:
                        homeActivity.DisplayFragmentMain();
                        break;
                    case 1:
                        homeActivity.DisplayFragmentnotifications();

                        break;
                    case 2:
                        homeActivity.DisplayFragmentSearch();

                        break;
                    case 3:
                        homeActivity.DisplayFragmentMore();
                        break;

                }
                return false;
            }
        });
    }

    private void setUpBottomNavigation() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getString(R.string.home), R.drawable.house);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getString(R.string.notifications), R.drawable.notifications);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getString(R.string.search), R.drawable.search);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem(getString(R.string.more), R.drawable.more);

        ah_bottom_nav.setTitleState(AHBottomNavigation.TitleState.ALWAYS_SHOW);
        ah_bottom_nav.setDefaultBackgroundColor(ContextCompat.getColor(homeActivity, R.color.white));
        ah_bottom_nav.setTitleTextSizeInSp(14, 12);
        ah_bottom_nav.setForceTint(true);
        ah_bottom_nav.setAccentColor(ContextCompat.getColor(homeActivity, R.color.colorAccent));
        ah_bottom_nav.setInactiveColor(ContextCompat.getColor(homeActivity, R.color.gray4));

        ah_bottom_nav.addItem(item1);
        ah_bottom_nav.addItem(item2);
        ah_bottom_nav.addItem(item3);
        ah_bottom_nav.addItem(item4);
        categories();

    }

    public void updateBottomNavigationPosition(int pos) {
        ah_bottom_nav.setCurrentItem(pos, false);
    }

    public static Fragment_Home newInstance() {
        return new Fragment_Home();
    }

    public void categories() {

        Api.getService().getcateogries(cuurent_language).enqueue(new Callback<Catogry_Model>() {
            @Override
            public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                //progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body().getCategories() != null && response.body().getCategories().size() > 0) {
                        categories.clear();
                        categories.addAll(response.body().getCategories());
                        catogriesAdapter.notifyDataSetChanged();
                        setsub();
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

    public void setsub(List<Catogry_Model.Categories.sub> subs, String main_category_fk) {
        homeActivity.setsub(subs,main_category_fk);
    }

    public void setsub() {
        if(categories.size()>0){
        homeActivity.setsub(categories.get(0).getsub(), categories.get(0).getMain_category_fk());}
    }
}
