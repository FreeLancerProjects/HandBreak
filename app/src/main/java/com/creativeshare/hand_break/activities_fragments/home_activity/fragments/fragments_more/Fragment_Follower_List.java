package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Followers_Adapter;
import com.creativeshare.hand_break.models.Follower_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Follower_List extends Fragment {
    private ProgressBar progBar;
    private LinearLayout ll_no_order;
    private RecyclerView rec_follow;
    private Followers_Adapter followers_adapter;
    private List<Follower_Model.Data> data;
    private HomeActivity homeActivity;
    private UserModel userModel;
    private Preferences preferences;
    private ImageView back;
    private String cuurent_language;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follower, container, false);
        initView(view);
        getfollowerList();
        return view;
    }

    private void getfollowerList() {
        rec_follow.setVisibility(View.GONE);
        ll_no_order.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);
        Api.getService().getfollowers(userModel.getUser_id(), "user").enqueue(new Callback<Follower_Model>() {
            @Override
            public void onResponse(Call<Follower_Model> call, Response<Follower_Model> response) {
                progBar.setVisibility(View.GONE);
                rec_follow.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    if (response.body().getData() != null && response.body().getData().size() > 0) {
                        data.clear();
                        data.addAll(response.body().getData());
                        followers_adapter.notifyDataSetChanged();
                    } else {
                        ll_no_order.setVisibility(View.VISIBLE);
                    }
                } else {

                    Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();
                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onFailure(Call<Follower_Model> call, Throwable t) {

                try {

                    progBar.setVisibility(View.GONE);
                    Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    private void initView(View view) {

        data = new ArrayList<>();
        rec_follow = view.findViewById(R.id.recView);
        progBar = view.findViewById(R.id.progBar);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        back = view.findViewById(R.id.arrow_back);


        homeActivity = (HomeActivity) getActivity();
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(homeActivity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());

        rec_follow.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        rec_follow.setDrawingCacheEnabled(true);
        rec_follow.setItemViewCacheSize(25);
        followers_adapter = new Followers_Adapter(data, homeActivity);
        rec_follow.setLayoutManager(new GridLayoutManager(homeActivity, 3));
        rec_follow.setAdapter(followers_adapter);
        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });
    }

    public static Fragment_Follower_List newInstance() {
        return new Fragment_Follower_List();
    }
}
