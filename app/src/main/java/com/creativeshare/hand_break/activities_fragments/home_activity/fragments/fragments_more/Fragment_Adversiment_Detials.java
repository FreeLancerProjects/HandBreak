package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.GalleryAdapter;
import com.creativeshare.hand_break.adapters.Same_Adversiment_GalleryAdapter;
import com.creativeshare.hand_break.adapters.SlidingImage_Adapter;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.AppDataModel;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.creativeshare.hand_break.share.Time_Ago;
import com.google.android.material.tabs.TabLayout;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Adversiment_Detials extends Fragment {
    private final static String Tag1 = "ads";
    private String id_advertisement;
    private HomeActivity activity;
    private String cuurent_language;
    private ImageView back;
    private TextView tv_title, tv_time, tv_coomericail, tv_name, tv_city, tv_desc, tv_phone;
    private ViewPager mPager;
    private TabLayout indicator;
    private RecyclerView recyclerView_images;

    private int current_page = 0, NUM_PAGES;
    private ProgressBar progBar;
    private SlidingImage_Adapter slidingImage__adapter;
    private Same_Adversiment_GalleryAdapter same_adversiment_galleryAdapter;
    private List<Adversiting_Model.Same_advertisements> advertisementsList;

    public static Fragment_Adversiment_Detials newInstance(String id_adversmentt) {
        Fragment_Adversiment_Detials fragment_adversiment_detials = new Fragment_Adversiment_Detials();
        Bundle bundle = new Bundle();
        bundle.putString(Tag1, id_adversmentt);
        fragment_adversiment_detials.setArguments(bundle);
        return fragment_adversiment_detials;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_adversiment_detials, container, false);
        intitview(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void intitview(View view) {
        advertisementsList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        back = view.findViewById(R.id.arrow_back);
        tv_title = view.findViewById(R.id.tv_title);
        tv_time = view.findViewById(R.id.tv_time);
        tv_coomericail = view.findViewById(R.id.tv_commercial);
        tv_name = view.findViewById(R.id.tv_name);
        tv_city = view.findViewById(R.id.tv_city);
        tv_desc = view.findViewById(R.id.tv_desc);
        tv_phone = view.findViewById(R.id.tv_phone);
        progBar = view.findViewById(R.id.progBar);
        mPager = view.findViewById(R.id.pager);
        indicator = view.findViewById(R.id.tablayout);
        recyclerView_images = view.findViewById(R.id.rec_images);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        // preferences = Preferences.getInstance();
        recyclerView_images.setDrawingCacheEnabled(true);
        recyclerView_images.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView_images.setItemViewCacheSize(25);
        same_adversiment_galleryAdapter = new Same_Adversiment_GalleryAdapter(advertisementsList, activity);
        recyclerView_images.setLayoutManager(new GridLayoutManager(activity, 3));
        recyclerView_images.setAdapter(same_adversiment_galleryAdapter);
        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }
        id_advertisement = getArguments().getString(Tag1);
        getadeversmentdetials(id_advertisement);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
            }
        });
        //preferences= Preferences.getInstance();
    }

    private void getadeversmentdetials(String id_advertisement) {
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().getadversmentdetials(id_advertisement).enqueue(new Callback<Adversiting_Model>() {
            @Override
            public void onResponse(Call<Adversiting_Model> call, Response<Adversiting_Model> response) {

                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    updateTermsContent(response.body());
                }
            }

            @Override
            public void onFailure(Call<Adversiting_Model> call, Throwable t) {
                try {
                    dialog.dismiss();
                    // smoothprogressbar.setVisibility(View.GONE);
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });

    }

    private void updateTermsContent(Adversiting_Model advertsing) {
        tv_time.setText(Time_Ago.getTimeAgo(Long.parseLong(advertsing.getAdvertisement_date()), activity));
        tv_title.setText(advertsing.getAdvertisement_title());
        Log.e("msg", Integer.parseInt(advertsing.getAdvertisement_date()) + "");
        tv_name.setText(advertsing.getUser_name());
        tv_phone.setText(advertsing.getPhone());
        tv_city.setText(advertsing.getCity_title());
        tv_desc.setText(advertsing.getAdvertisement_content());
        tv_coomericail.setText(advertsing.getAdvertisement_code());
        progBar.setVisibility(View.GONE);
        NUM_PAGES = advertsing.getAdvertisement_images().size();
        slidingImage__adapter = new SlidingImage_Adapter(activity, advertsing.getAdvertisement_images());
        mPager.setAdapter(slidingImage__adapter);
        indicator.setupWithViewPager(mPager);
        change_slide_image();
        advertisementsList.clear();
        advertisementsList.addAll(advertsing.getSame_advertisements());
        same_adversiment_galleryAdapter.notifyDataSetChanged();
    }

    private void change_slide_image() {
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (current_page == NUM_PAGES) {
                    current_page = 0;
                }
                mPager.setCurrentItem(current_page++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }


}
