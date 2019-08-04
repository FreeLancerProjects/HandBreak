package com.creativeshare.hand_break.activities_fragments.home_activity.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.location.Location;
import android.media.Image;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Adversiment_Adapter;
import com.creativeshare.hand_break.adapters.CatogriesAdapter;
import com.creativeshare.hand_break.adapters.Spinner_catogry_Adapter;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.adapters.Spinner_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_Sub_catogry_Adapter;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Main extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private List<Catogry_Model.Categories> subs;
    private Spinner_catogry_Adapter spinner_catogry_adapter;
    private Spinner sub_cat, cities;
    private Spinner_Adapter city_adapter;
    private List<CityModel> cities_models;
    private RecyclerView rec_search;
    private RecyclerView rec_catogry;
    private CatogriesAdapter catogriesAdapter;
    // private List<Catogry_Model.Categories> categories;

    private ImageView im_search;
    private Adversiment_Adapter adversiment_adapter;
    private List<Catogry_Model.Categories> categories1;
    private List<Catogry_Model.Advertsing> advertsings;
    private Preferences preferences;
    private UserModel userModel;
    private String maincatogryfk;
    private ProgressBar progBar;
    private LinearLayout ll_no_order;
    private boolean isLoading = false;
    private int current_page = 1;
    private GridLayoutManager manager;
    private String user_id = "all";
    private String city_id = "all";
    private String sub_id = "all";
    private int total_page;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    private double lat = 0.0, lang = 0.0;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private TextView tv_near;
    private int type;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        initView(view);
        CheckPermission();

        getCities();
        categories();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1255) {
            if (resultCode == Activity.RESULT_OK) {
                startLocationUpdate();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == gps_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            initGoogleApiClient();
        }
    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(homeActivity, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(homeActivity, new String[]{gps_perm}, gps_req);
        } else {

            initGoogleApiClient();
        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(homeActivity)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void initView(View view) {
        sub_cat = view.findViewById(R.id.sub_cat);
        cities = view.findViewById(R.id.sp_city);
        rec_search = view.findViewById(R.id.rec_search);
        progBar = view.findViewById(R.id.progBar);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        im_search = view.findViewById(R.id.im_search);

        categories1 = new ArrayList<>();
        rec_catogry = view.findViewById(R.id.rec_data);
        tv_near = view.findViewById(R.id.tv_near);

        rec_catogry.setLayoutManager(new LinearLayoutManager(homeActivity, RecyclerView.HORIZONTAL, false));
        //rec_catogry.setAdapter(catogriesAdapter);
        subs = new ArrayList<>();
        //categories = new ArrayList<>();
        advertsings = new ArrayList<>();

        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        catogriesAdapter = new CatogriesAdapter(categories1, homeActivity, this);
        rec_catogry.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        rec_catogry.setDrawingCacheEnabled(true);
        rec_catogry.setItemViewCacheSize(25);
        rec_catogry.setAdapter(catogriesAdapter);
        if (userModel == null) {
            user_id = "";
        } else {
            user_id = userModel.getUser_id();
        }

        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(homeActivity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        cities_models = new ArrayList<>();
        if (cuurent_language.equals("ar")) {
            cities_models.add(new CityModel("مدينتى"));
            subs.add(new Catogry_Model.Categories(" كل الاقسام"));

        } else {
            cities_models.add(new CityModel("City"));
            subs.add(new Catogry_Model.Categories("Department"));

        }

        spinner_catogry_adapter = new Spinner_catogry_Adapter(homeActivity, subs);
        sub_cat.setAdapter(spinner_catogry_adapter);
        city_adapter = new Spinner_Adapter(homeActivity, cities_models);
        cities.setAdapter(city_adapter);
        adversiment_adapter = new Adversiment_Adapter(advertsings, homeActivity);
        rec_search.setDrawingCacheEnabled(true);
        rec_search.setItemViewCacheSize(25);
        rec_search.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        manager = new GridLayoutManager(homeActivity, 1);
        rec_search.setLayoutManager(manager);
        rec_search.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int total_item = manager.getItemCount();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                    //   Log.e("msg", total_item + "  " + last_item_pos);
                    // Log.e("msg", current_page+"");

                    if (last_item_pos >= (total_item - 5) && !isLoading && total_page > current_page) {
                        isLoading = true;
                        advertsings.add(null);
                        adversiment_adapter.notifyItemInserted(advertsings.size() - 1);
                        int page = current_page + 1;
                        //cuurent_language+=1;
                        //   Log.e("msg", page+"");
                        if (type == 2) {
                            loadMore(page);
                        } else if (type == 1) {
                            loadnearMore(page);
                        } else if (type == 3) {
                            loadMoremain(page);
                        }
                    }
                }
            }

        });
        rec_search.setAdapter(adversiment_adapter);

        sub_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sub_id = "all";

                } else {
                    sub_id = subs.get(i).getMain_category_fk();
                    getadversment();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    city_id = "all";
                } else {
                    city_id = cities_models.get(i).getId_city();
                    getadversment();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        im_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getadversment();
            }
        });
        tv_near.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_id = "all";
                sub_id = "all";
                cities.setSelection(0);
                sub_cat.setSelection(0);
                getnearadversment();
            }
        });
    }

    public static Fragment_Main newInstance() {
        return new Fragment_Main();
    }

  /*
    public void addsubtosppinner(List<Catogry_Model.Categories> subs, String maincatogryfk) {

        sub_cat.setSelection(0);
        this.maincatogryfk = maincatogryfk;
        city_id = "all";
        sub_id = "all";
        cities.setSelection(0);


        getnearadversment();
    }*/

    private void getnearadversment() {
        rec_search.setVisibility(View.GONE);
        ll_no_order.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);
        type = 1;

        Api.getService()
                .getnearadversment(1, user_id, lat + "", lang + "")
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        progBar.setVisibility(View.GONE);
                        rec_search.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {
                            advertsings.clear();
                            advertsings.addAll(response.body().getAdvertsing());
                            //categories.clear();
                            //categories.addAll(response.body().getCategories());
                            //total_page = response.body().getMeta().getLast_page();
                            if (advertsings.size() > 0) {
                                //ll_no_order.setVisibility(View.GONE);
                                adversiment_adapter.notifyDataSetChanged();
                                total_page = response.body().getMeta().getLast_page();
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

    private void getCities() {

        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
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
                                    cities_models.add(new CityModel("مدينتى"));
                                } else {
                                    cities_models.add(new CityModel("City"));

                                }
                                cities_models.addAll(response.body());
                                city_adapter.notifyDataSetChanged();
                            }
                        } else {
                            try {
                                Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {

                        }
                    }
                });

    }

    private void getadversment() {
        type = 2;
        rec_search.setVisibility(View.GONE);
        ll_no_order.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);


        Api.getService()
                .getadversment(1, user_id, sub_id, city_id)
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        progBar.setVisibility(View.GONE);
                        rec_search.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {
                            advertsings.clear();
                            advertsings.addAll(response.body().getAdvertsing());
                            //categories.clear();
                            //categories.addAll(response.body().getCategories());
                            //total_page = response.body().getMeta().getLast_page();
                            if (advertsings.size() > 0) {
                                //ll_no_order.setVisibility(View.GONE);
                                adversiment_adapter.notifyDataSetChanged();
                                total_page = response.body().getMeta().getLast_page();
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

    private void loadMore(int page) {


        Api.getService()
                .getadversment(page, user_id, sub_id, city_id + "")
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        advertsings.remove(advertsings.size() - 1);
                        adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {

                            advertsings.addAll(response.body().getAdvertsing());
                            //categories.addAll(response.body().getCategories());

                            adversiment_adapter.notifyDataSetChanged();

                            current_page = response.body().getMeta().getCurrent_page();

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
                            advertsings.remove(advertsings.size() - 1);
                            adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                            // isLoading = false;
                            Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void loadnearMore(int page) {


        Api.getService()
                .getnearadversment(page, user_id, lat + "", lang + "")
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        advertsings.remove(advertsings.size() - 1);
                        adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {

                            advertsings.addAll(response.body().getAdvertsing());
                            //categories.addAll(response.body().getCategories());

                            adversiment_adapter.notifyDataSetChanged();

                            current_page = response.body().getMeta().getCurrent_page();

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
                            advertsings.remove(advertsings.size() - 1);
                            adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                            // isLoading = false;
                            Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }

    private void getadversmentmain() {
        type = 3;
        rec_search.setVisibility(View.GONE);
        ll_no_order.setVisibility(View.GONE);
        progBar.setVisibility(View.VISIBLE);


        Api.getService()
                .getadversment(1, user_id, maincatogryfk)
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        progBar.setVisibility(View.GONE);
                        rec_search.setVisibility(View.VISIBLE);
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {
                            advertsings.clear();
                            advertsings.addAll(response.body().getAdvertsing());
                            //categories.clear();
                            //categories.addAll(response.body().getCategories());
                            //total_page = response.body().getMeta().getLast_page();
                            if (advertsings.size() > 0) {
                                //ll_no_order.setVisibility(View.GONE);
                                adversiment_adapter.notifyDataSetChanged();
                                total_page = response.body().getMeta().getLast_page();
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

    private void loadMoremain(int page) {


        Api.getService()
                .getadversment(page, user_id, maincatogryfk)
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        advertsings.remove(advertsings.size() - 1);
                        adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {

                            advertsings.addAll(response.body().getAdvertsing());
                            //categories.addAll(response.body().getCategories());

                            adversiment_adapter.notifyDataSetChanged();

                            current_page = response.body().getMeta().getCurrent_page();

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
                            advertsings.remove(advertsings.size() - 1);
                            adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                            // isLoading = false;
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
                        categories1.clear();
                        categories1.addAll(response.body().getCategories());
                        catogriesAdapter.notifyDataSetChanged();
                        maincatogryfk = response.body().getCategories().get(0).getMain_category_fk();
                        subs.clear();
                        if (cuurent_language.equals("ar")) {
                            subs.add(new Catogry_Model.Categories("الاقسام"));

                        } else {
                            subs.add(new Catogry_Model.Categories("Department"));

                        }
                        subs.addAll(response.body().getCategories());
                        spinner_catogry_adapter.notifyDataSetChanged();
                        //    addsubtosppinner(response.body().getCategories().get(0).getsub(), response.body().getCategories().get(0).getMain_category_fk());
                        //getadversment();
                        getadversmentmain();
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

    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(homeActivity, 1255);
                        } catch (Exception e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;
        lang = location.getLongitude();
        lat = location.getLatitude();
        //Log.e("lang", lang + "  " + lat);
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }

        if (locationCallback != null) {
            LocationServices.getFusedLocationProviderClient(homeActivity).removeLocationUpdates(locationCallback);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(homeActivity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    public void searchmain(String main_category_fk) {
        this.maincatogryfk = main_category_fk;
        getadversmentmain();
    }
}
