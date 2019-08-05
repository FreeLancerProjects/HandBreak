package com.creativeshare.hand_break.activities_fragments.home_activity.fragments;

import android.app.ProgressDialog;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
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
import com.creativeshare.hand_break.adapters.Spinner_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_Sub_Sub_catogry_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_Sub_catogry_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_catogry_Adapter;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.CityModel;
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
    private Spinner cities;
    private Spinner_Sub_catogry_Adapter spinner_sub_catogry_adapter;
    private Spinner_catogry_Adapter spinner_catogry_adapter;
    private Spinner_Sub_Sub_catogry_Adapter spinner_sub_sub_catogry_adapter;
    private Spinner sp_cat;
   // , sp_sub_cat, sp_model
    private ImageView im_search;
    private EditText edt_name;
    private Button bt_search;
    private ScrollView scrollView;
    private ConstraintLayout cons;
    private RecyclerView rec_search;
    private ProgressBar progBar;
    private LinearLayout ll_no_order;
    private Spinner_Adapter city_adapter;
    private List<CityModel> cities_models;
//    private List<Catogry_Model.Categories.sub> subs;
    private List<Catogry_Model.Categories> categories;
   // private List<Catogry_Model.Categories> categories2;

  //  private List<Catogry_Model.Categories.sub.Sub> subs_sub;
    private boolean isLoading = false;
    private int current_page = 1;
    private GridLayoutManager manager;
    private String user_id="all";
    private Preferences preferences;
    private UserModel userModel;
    private List<Catogry_Model.Advertsing> advertsings;
   // private List<Catogry_Model.Categories> categories;
   // sub_id,model_id,platenumber,
   private String city_id="all",cat_id="all",typeused;
    private RadioGroup group_type;
//private EditText edt_plate;
    private Adversiment_Adapter adversiment_adapter;

    private String search;
    private int type = -1;
    private int total_page;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initView(view);
        getCities();
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
  //      edt_plate=view.findViewById(R.id.edt_plate);
        group_type=view.findViewById(R.id.group_type);
        scrollView = view.findViewById(R.id.scrollable);
        rec_search = view.findViewById(R.id.rec_search);
        progBar = view.findViewById(R.id.progBar);
        ll_no_order = view.findViewById(R.id.ll_no_order);
        cons = view.findViewById(R.id.cons1);
        cities = view.findViewById(R.id.sp_city);
        sp_cat = view.findViewById(R.id.sp_cat);
      //  sp_sub_cat = view.findViewById(R.id.sp_sub_cat);
       // sp_model = view.findViewById(R.id.sp_model);
        bt_search=view.findViewById(R.id.bt_search);
        //cities_models = new ArrayList<>();
        //categories2 = new ArrayList<>();
    //    subs = new ArrayList<>();
      //  subs_sub = new ArrayList<>();
        cities_models = new ArrayList<>();
        if (cuurent_language.equals("ar")) {
            cities_models.add(new CityModel("مدينتى"));
         //   subs.add(new Catogry_Model.Categories.sub("الكل"));
        //    subs.add(new Catogry_Model.Categories.sub("النوع"));
            categories.add(new Catogry_Model.Categories("كل الاقسام"));
          //  subs_sub.add(new Catogry_Model.Categories.sub.Sub("الموديل"));
        } else {
            cities_models.add(new CityModel("City"));
           // subs.add(new Catogry_Model.Categories.sub("all"));
            //subs.add(new Catogry_Model.Categories.sub("Type"));
            categories.add(new Catogry_Model.Categories("all department"));
            //subs_sub.add(new Catogry_Model.Categories.sub.Sub("Model"));
        }

        city_adapter = new Spinner_Adapter(homeActivity, cities_models);
        spinner_catogry_adapter = new Spinner_catogry_Adapter(homeActivity, categories);
        //spinner_sub_catogry_adapter = new Spinner_Sub_catogry_Adapter(homeActivity, subs);
       // spinner_sub_sub_catogry_adapter = new Spinner_Sub_Sub_catogry_Adapter(homeActivity, subs_sub);
        sp_cat.setAdapter(spinner_catogry_adapter);
      //  sp_sub_cat.setAdapter(spinner_sub_catogry_adapter);
        //sp_model.setAdapter(spinner_sub_sub_catogry_adapter);
        cities.setAdapter(city_adapter);
        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i>0){
                    city_id=cities_models.get(i).getId_city();
                }
                else {
                    city_id="all";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
         /*       subs.clear();
                if (cuurent_language.equals("ar")) {
                    subs.add(new Catogry_Model.Categories.sub("النوع"));

                } else {
                    subs.add(new Catogry_Model.Categories.sub("Type"));

                }*/
                if (i > 0 ) {
                   // subs.addAll(categories.get(i).getsub());
                   // spinner_sub_catogry_adapter.notifyDataSetChanged();
                    cat_id=categories.get(i).getMain_category_fk();
                }
                else {
                    cat_id="all";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });/*
        sp_sub_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // subs_sub.clear();
                if (cuurent_language.equals("ar")) {
                    subs_sub.add(new Catogry_Model.Categories.sub.Sub("الموديل"));

                } else {
                    subs_sub.add(new Catogry_Model.Categories.sub.Sub("Model"));

                }if (i > 0 && subs.get(i).getSubs() != null) {
                    subs_sub.addAll(subs.get(i).getSubs());
                    spinner_sub_sub_catogry_adapter.notifyDataSetChanged();
                    //   Log.e("sssdd",subs.get(i).getSub_category_fk());

                }
                if(i>0){
                    sub_id=subs.get(i).getSub_category_fk();

                }
                else {
                    sub_id="all";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0 ) {
                    model_id=subs_sub.get(i).getModel_id_fk();

                }
                else {
                    model_id = "all";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });*/
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(homeActivity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
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
                    int total_item = adversiment_adapter.getItemCount();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                   // Log.e("msg", total_item + "  " + last_item_pos);
                    if (last_item_pos >= (total_item - 5) && !isLoading && total_page > current_page) {
                        isLoading = true;
                        advertsings.add(null);
                        adversiment_adapter.notifyItemInserted(advertsings.size() - 1);
                        int page = current_page + 1;
                        if (type == 0) {
                            loadMore(page);
                        }
                        else if(type==1){
                            loadMore2(page);
                        }
                    }
                }
            }
        });
        rec_search.setAdapter(adversiment_adapter);

        if (userModel == null) {
            user_id = "all";
        } else {
            user_id = userModel.getUser_id();
        }
        im_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollView.setVisibility(View.GONE);
                cons.setVisibility(View.VISIBLE);
                if (edt_name.getText().toString() != null) {
                    current_page=1;
                    advertsings.clear();

                    search = edt_name.getText().toString();
                    type = 0;
                    searchadversment();
                } else {
                    edt_name.setError(getResources().getString(R.string.field_req));
                }
            }
        });
bt_search.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        scrollView.setVisibility(View.GONE);
        cons.setVisibility(View.VISIBLE);
        current_page=1;
        advertsings.clear();
        type=1;
        /*if(TextUtils.isEmpty(edt_plate.getText().toString())){
            platenumber="all";
        }
        else {

            platenumber=edt_plate.getText().toString();
        }*/
        if(group_type.getCheckedRadioButtonId()==R.id.r_new){
           // adversiment_model.setType("1");
            typeused="1";
        }

        else if(group_type.getCheckedRadioButtonId()==R.id.r_used){
          //  adversiment_model.setType("2");
            typeused="2";

        }
searchadversment(city_id,cat_id,typeused);
    }
});
    }

    private void searchadversment(String city_id, String cat_id, String typeused) {
        progBar.setVisibility(View.VISIBLE);
        ll_no_order.setVisibility(View.GONE);

        Api.getService()
                .searchadversment2(1, user_id,city_id,cat_id,typeused)
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
                .searchadversment(page, user_id , search)
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        advertsings.remove(advertsings.size() - 1);
                        adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {

                            advertsings.addAll(response.body().getAdvertsing());
                           // categories.addAll(response.body().getCategories());
                            current_page = response.body().getMeta().getCurrent_page();
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
                            advertsings.remove(advertsings.size() - 1);
                            adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                            isLoading = false;
                            Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });
    }
    private void loadMore2(int page) {
        Api.getService()
                .searchadversment2(page, user_id,city_id , cat_id,typeused)
                .enqueue(new Callback<Catogry_Model>() {
                    @Override
                    public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                        advertsings.remove(advertsings.size() - 1);
                        adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
                        isLoading = false;
                        if (response.isSuccessful() && response.body() != null && response.body().getAdvertsing() != null) {

                            advertsings.addAll(response.body().getAdvertsing());
                            //categories.addAll(response.body().getCategories());
                            current_page = response.body().getMeta().getCurrent_page();
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
                            advertsings.remove(advertsings.size() - 1);
                            adversiment_adapter.notifyItemRemoved(advertsings.size() - 1);
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
    public void categories() {

        Api.getService().getcateogries(cuurent_language).enqueue(new Callback<Catogry_Model>() {
            @Override
            public void onResponse(Call<Catogry_Model> call, Response<Catogry_Model> response) {
                //progBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    if (response.body().getCategories() != null && response.body().getCategories().size() > 0) {
                        categories.clear();
                        //categories2.clear();
                        if(cuurent_language.equals("ar")){
                            categories.add(new Catogry_Model.Categories("كل الاقسام"));

                        }
                        else {
                            categories.add(new Catogry_Model.Categories("all department"));
                        }
                        categories.addAll(response.body().getCategories());
                       // categories2.addAll(response.body().getCategories());

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
