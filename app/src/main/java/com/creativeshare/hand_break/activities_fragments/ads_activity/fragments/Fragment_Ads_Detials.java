package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.GalleryAdapter;
import com.creativeshare.hand_break.adapters.ShowGalleryAdapter;
import com.creativeshare.hand_break.adapters.Spinner_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_Sub_Sub_catogry_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_Sub_catogry_Adapter;
import com.creativeshare.hand_break.adapters.Spinner_catogry_Adapter;
import com.creativeshare.hand_break.adapters.ViewPagerAdapter;
import com.creativeshare.hand_break.models.Adversiment_Model;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;

import java.io.IOException;
import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Ads_Detials extends Fragment {
    private AdsActivity adsActivity;
    private Preferences preferences;
    private UserModel userModel;
    private String cuurent_language;
    private EditText edt_plate;
    private RadioGroup group_type;
    private LinearLayout ll_continue;
    private ImageView bt_arrow;
    private TextView tv_terms;
    private CheckBox checkBox;


    private Spinner_Sub_catogry_Adapter spinner_sub_catogry_adapter;
    private Spinner_catogry_Adapter spinner_catogry_adapter;
    private Spinner_Sub_Sub_catogry_Adapter spinner_sub_sub_catogry_adapter;
    private Spinner sp_cat, sp_sub_cat, sp_model, cities;
    private Spinner_Adapter city_adapter;
    private List<CityModel> cities_models;
    private LinearLayout ll_cv;
    private RecyclerView recyclerView_images;
    private GalleryAdapter galleryAdapter;
    private RecyclerView recyclerViewshow_images;
    private ShowGalleryAdapter showgalleryAdapter;
    private List<Uri> uriList;
    private List<Catogry_Model.Categories.sub> subs;
    private List<Catogry_Model.Categories> categories;
    private List<Catogry_Model.Categories.sub.Sub> subs_sub;
    private final int IMG2 = 2;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    private String city_id, cat_id, sub_id, model_id;
    private Adversiment_Model adversiment_model;
private List<Adversiting_Model.Advertisement_images> advertisement_images;
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
        adversiment_model = new Adversiment_Model();
        advertisement_images=new ArrayList<>();
        adsActivity = (AdsActivity) getActivity();
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
       // edt_piece=view.findViewById(R.id.edt_piece);
        edt_plate=view.findViewById(R.id.edt_plate);
        group_type=view.findViewById(R.id.group_type);
        ll_continue = view.findViewById(R.id.ll_continue);
        bt_arrow = view.findViewById(R.id.bt_arrow);
        tv_terms = view.findViewById(R.id.tv_terms);
        sp_cat = view.findViewById(R.id.sp_cat);
        ll_cv = view.findViewById(R.id.ll_cv);
        checkBox = view.findViewById(R.id.checkbox);
        recyclerView_images = view.findViewById(R.id.recView_images);
        recyclerViewshow_images = view.findViewById(R.id.recView_show_images);

        sp_sub_cat = view.findViewById(R.id.sp_sub);
        sp_model = view.findViewById(R.id.sp_model);
        cities_models = new ArrayList<>();
        categories = new ArrayList<>();
        subs = new ArrayList<>();
        subs_sub = new ArrayList<>();
        uriList = new ArrayList<>();
        recyclerView_images.setDrawingCacheEnabled(true);
        recyclerView_images.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView_images.setItemViewCacheSize(25);
        galleryAdapter = new GalleryAdapter(uriList, adsActivity, this);
        recyclerView_images.setLayoutManager(new LinearLayoutManager(adsActivity, LinearLayoutManager.HORIZONTAL, true));
        recyclerView_images.setAdapter(galleryAdapter);
        recyclerViewshow_images.setDrawingCacheEnabled(true);
        recyclerViewshow_images.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerViewshow_images.setItemViewCacheSize(25);
        showgalleryAdapter = new ShowGalleryAdapter(advertisement_images, adsActivity, this);
        recyclerViewshow_images.setLayoutManager(new LinearLayoutManager(adsActivity, LinearLayoutManager.HORIZONTAL, true));
        recyclerViewshow_images.setAdapter(showgalleryAdapter);
        if (cuurent_language.equals("ar")) {
            cities_models.add(new CityModel("مدينتى "));
            subs.add(new Catogry_Model.Categories.sub("النوع"));
            categories.add(new Catogry_Model.Categories("كل الاقسام"));
            subs_sub.add(new Catogry_Model.Categories.sub.Sub("الموديل"));

        } else {
            cities_models.add(new CityModel("City"));
            subs.add(new Catogry_Model.Categories.sub("Type"));
            categories.add(new Catogry_Model.Categories("all departments"));
            subs_sub.add(new Catogry_Model.Categories.sub.Sub("model"));


        }
        if(!Adversiment_Model.getId().equals("-1")){
            recyclerViewshow_images.setVisibility(View.VISIBLE);
            getadeversmentdetials(Adversiment_Model.getId());
            checkBox.setChecked(true);
        }


        cities = view.findViewById(R.id.sp_city);
        spinner_catogry_adapter = new Spinner_catogry_Adapter(adsActivity, categories);
        spinner_sub_catogry_adapter = new Spinner_Sub_catogry_Adapter(adsActivity, subs);
        spinner_sub_sub_catogry_adapter = new Spinner_Sub_Sub_catogry_Adapter(adsActivity, subs_sub);
        sp_cat.setAdapter(spinner_catogry_adapter);
        sp_sub_cat.setAdapter(spinner_sub_catogry_adapter);
        sp_model.setAdapter(spinner_sub_sub_catogry_adapter);
        city_adapter = new Spinner_Adapter(adsActivity, cities_models);
        cities.setAdapter(city_adapter);
        if (cuurent_language.equals("en")) {
            bt_arrow.setRotation(180.0f);
        }
        tv_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adsActivity.DisplayFragmentterms();
            }
        });
        ll_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG2);
            }
        });
        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    city_id = cities_models.get(i).getId_city();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subs.clear();
                if (cuurent_language.equals("ar")) {
                    subs.add(new Catogry_Model.Categories.sub("النوع"));

                } else {
                    subs.add(new Catogry_Model.Categories.sub("Type"));

                }
                sp_sub_cat.setSelection(0);
                if (i > 0 && categories.get(i).getsub() != null) {
                    subs.addAll(categories.get(i).getsub());
                    spinner_sub_catogry_adapter.notifyDataSetChanged();
                    cat_id = categories.get(i).getMain_category_fk();
                }
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
                    subs_sub.add(new Catogry_Model.Categories.sub.Sub("الموديل"));

                } else {
                    subs_sub.add(new Catogry_Model.Categories.sub.Sub("model"));

                }
                sp_model.setSelection(0);
                if (i > 0 && subs.get(i).getSubs() != null) {
                    subs_sub.addAll(subs.get(i).getSubs());
                    spinner_sub_sub_catogry_adapter.notifyDataSetChanged();
                    //   Log.e("sssdd",subs.get(i).getSub_category_fk());

                }
                if (i > 0) {
                    sub_id = subs.get(i).getSub_category_fk();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sp_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    model_id = subs_sub.get(i).getModel_id_fk();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ll_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // String piecen=edt_piece.getText().toString();
                String plate=edt_plate.getText().toString();
                if (checkBox.isChecked()) {
                    if (city_id != null && cat_id != null && sub_id != null && (uriList.size() > 0 || !Adversiment_Model.getId().equals("-1"))) {
                        adversiment_model.setCity_id(city_id);
                        adversiment_model.setCat_id(cat_id);
                        adversiment_model.setSub_id(sub_id);
                        adversiment_model.setUris(uriList);
                    /*    if(TextUtils.isEmpty(piecen)){
                            piecen="0";
                        }*/
                        if(TextUtils.isEmpty(plate)){
                            plate="0";
                        }
                        if(group_type.getCheckedRadioButtonId()==R.id.r_new){
                            adversiment_model.setType("1");
                        }
                        else if(group_type.getCheckedRadioButtonId()==R.id.r_used){
                            adversiment_model.setType("2");

                        }
                        adversiment_model.setPalte(plate);
                   //     adversiment_model.setPiece(piecen);
                        if (model_id != null) {
                            adversiment_model.setModel_id(model_id);
                            adsActivity.gotonext(adversiment_model);

                        } else if (subs_sub.size() == 1) {
                            adversiment_model.setModel_id("no_model_id");
                            adsActivity.gotonext(adversiment_model);


                        } else {
                            Common.CreateSignAlertDialog(adsActivity, getString(R.string.complete_all));

                        }
                    } else {
                        Common.CreateSignAlertDialog(adsActivity, getString(R.string.complete_all));
                        // Log.e("ssss",subs_sub.size()+"'"+cat_id+" "+city_id+"  "+sub_id);

                    }
                } else {
                    Common.CreateSignAlertDialog(adsActivity, getString(R.string.apply_terms_and_conditions));
                }
            }
        });

    }

    private void getadeversmentdetials(String id_advertisement) {
        final ProgressDialog dialog = Common.createProgressDialog(adsActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().getadversmentdetials(id_advertisement,"all").enqueue(new Callback<Adversiting_Model>() {
            @Override
            public void onResponse(Call<Adversiting_Model> call, Response<Adversiting_Model> response) {

                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                   // updateTermsContent(response.body());
                  updatedata(response.body());
                   Adversiment_Model.setAdversiting_model(response.body());
                }
            }

            @Override
            public void onFailure(Call<Adversiting_Model> call, Throwable t) {
                try {
                    dialog.dismiss();
                    // smoothprogressbar.setVisibility(View.GONE);
                    Toast.makeText(adsActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });

    }

    private void updatedata(Adversiting_Model body) {
        advertisement_images.clear();
        advertisement_images.addAll(body.getAdvertisement_images());

        showgalleryAdapter.notifyDataSetChanged();
        if(body.getAdvertisement_type().equals("1")){
            group_type.check(R.id.r_new);
        }
        else {
            group_type.check(R.id.r_used);
        }
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
                                    cities_models.add(new CityModel("مدينتى"));
                                } else {
                                    cities_models.add(new CityModel("City"));

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
                        categories.clear();
                        if (cuurent_language.equals("ar")) {
                            categories.add(new Catogry_Model.Categories("كل الاقسام"));

                        } else {
                            categories.add(new Catogry_Model.Categories("all departments"));
                        }
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

    public void Delete(int position) {
        uriList.remove(position);
        galleryAdapter.notifyDataSetChanged();

    }

    private void Check_ReadPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(adsActivity, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(adsActivity, new String[]{read_permission}, img_req);
        } else {
            select_photo(img_req);
        }
    }

    private void select_photo(int img1) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        } else {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (img1 == 2) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }

        }
        intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setType("image/*");
        startActivityForResult(intent, img1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG2 && resultCode == Activity.RESULT_OK && data != null) {

            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            if (data.getData() != null) {

                Uri mImageUri = data.getData();

                // Get the cursor
                Cursor cursor = adsActivity.getContentResolver().query(mImageUri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();


                cursor.close();

                ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                mArrayUri.add(mImageUri);

                uriList.addAll(mArrayUri);

                galleryAdapter.notifyDataSetChanged();
                Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());

                //uploadCV();
            } else {

                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();
                    ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                    for (int i = 0; i < mClipData.getItemCount(); i++) {

                        ClipData.Item item = mClipData.getItemAt(i);
                        Uri uri = item.getUri();
                        mArrayUri.add(uri);
                        // Get the cursor
                        Cursor cursor = adsActivity.getContentResolver().query(uri, filePathColumn, null, null, null);
                        // Move to first row
                        cursor.moveToFirst();


                        cursor.close();
                    }
                    uriList.addAll(mArrayUri);

                    galleryAdapter.notifyDataSetChanged();

                    // uploadCV();
                    Log.v("LOG_TAG", "Selected Images" + mArrayUri.size());
                }

            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG2) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG2);
                } else {
                    Toast.makeText(adsActivity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public void Deleteimageapi(String image_id) {
        final ProgressDialog dialog = Common.createProgressDialog(adsActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
Api.getService().deleteimageads(Adversiment_Model.getId(),image_id).enqueue(new Callback<Adversiting_Model>() {
    @Override
    public void onResponse(Call<Adversiting_Model> call, Response<Adversiting_Model> response) {
        dialog.dismiss();
        if(response.isSuccessful()){
            Log.e("msg",response.code()+"");

advertisement_images.clear();
advertisement_images.addAll(response.body().getAdvertisement_images());
showgalleryAdapter.notifyDataSetChanged();
        }
        else {
            Log.e("msg",response.code()+"");
        }
    }

    @Override
    public void onFailure(Call<Adversiting_Model> call, Throwable t) {
dialog.dismiss();

        Log.e("msg",t.getMessage()+"");

    }
});
    }
}
