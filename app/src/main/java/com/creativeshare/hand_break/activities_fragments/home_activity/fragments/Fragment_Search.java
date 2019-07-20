package com.creativeshare.hand_break.activities_fragments.home_activity.fragments;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Adversiment_Adapter;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
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

public class Fragment_Search extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private ImageView im_search;
    private EditText edt_name;
    private ScrollView scrollView;
    private ConstraintLayout cons;
    private RecyclerView rec_search;
    private ProgressBar progBar;
    private LinearLayout ll_no_order;

    private boolean isLoading = false;
    private int current_page = 1;
    private GridLayoutManager manager;
    private String user_id;
    private Preferences preferences;
    private UserModel userModel;
    private List<Catogry_Model.Advertsing> advertsings;
    private List<Catogry_Model.Categories> categories;

    private Adversiment_Adapter adversiment_adapter;
    private String search;
    private int type = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        categories();
        return view;
    }

    private void initView(View view) {
        advertsings = new ArrayList<>();
        categories = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        edt_name = view.findViewById(R.id.edt_name);
        im_search = view.findViewById(R.id.im_search);
        scrollView = view.findViewById(R.id.scrollable);
        rec_search = view.findViewById(R.id.rec_search);
        progBar = view.findViewById(R.id.progBar);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        cons = view.findViewById(R.id.cons1);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(homeActivity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        adversiment_adapter = new Adversiment_Adapter(advertsings, categories, homeActivity);
        rec_search.setDrawingCacheEnabled(true);
        rec_search.setItemViewCacheSize(25);
        rec_search.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        manager = new GridLayoutManager(homeActivity, 1);
        rec_search.setLayoutManager(manager);
        rec_search.setAdapter(adversiment_adapter);
        rec_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int total_item = adversiment_adapter.getItemCount();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                    Log.e("msg", total_item + "  " + last_item_pos);
                    if (total_item > 5 && last_item_pos == (total_item - 5) && !isLoading) {
                        isLoading = true;
                        advertsings.add(null);
                        adversiment_adapter.notifyItemInserted(advertsings.size() - 1);
                        int page = current_page + 1;
                        if (type == 0) {
                            loadMore(page);
                        }
                    }
                }
            }
        });
        if (userModel == null) {
            user_id = "o";
        } else {
            user_id = userModel.getUser_id();
        }
        im_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.GONE);
                cons.setVisibility(View.VISIBLE);
                if (edt_name.getText().toString() != null) {
                    search = edt_name.getText().toString();
                    type = 0;
                    searchadversment();
                } else {
                    edt_name.setError(getResources().getString(R.string.field_req));
                }
            }
        });

    }

    private void loadMore(int page) {
        Api.getService()
                .searchadversment(1, user_id + "", search)
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        advertsings.remove(advertsings.size() - 1);
                        adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {

                            advertsings.addAll(response.body().getAdvertsing());
                            categories.addAll(response.body().getCategories());

                            adversiment_adapter.notifyDataSetChanged();

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
                    public void onFailure(Call<Catogry_Model> call, Throwable t) {
                        try {
                            isLoading = false;
                            Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    public static Fragment_Search newInstance() {
        return new Fragment_Search();
    }

    public void changevisible() {
        cons.setVisibility(View.GONE);

        scrollView.setVisibility(View.VISIBLE);
    }

    public void searchadversment() {
        progBar.setVisibility(View.VISIBLE);
        ll_no_order.setVisibility(View.GONE);

        Api.getService()
                .searchadversment(1, user_id + "", search)
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {
                            advertsings.clear();
                            advertsings.addAll(response.body().getAdvertsing());

                            if (advertsings.size() > 0) {
                                ll_no_order.setVisibility(View.GONE);
                                adversiment_adapter.notifyDataSetChanged();

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
                    public void onFailure(Call<Catogry_Model> call, Throwable t) {
                        try {

                            progBar.setVisibility(View.GONE);
                            Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
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
                        categories.clear();
                        categories.addAll(response.body().getCategories());
                        //catogriesAdapter.notifyDataSetChanged();
                        //  setsub();
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
