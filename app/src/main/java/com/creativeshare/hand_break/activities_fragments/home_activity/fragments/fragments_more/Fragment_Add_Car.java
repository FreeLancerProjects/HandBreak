package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
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
import com.creativeshare.hand_break.models.Adversiment_Model;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;

import java.io.IOException;
import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Add_Car extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private Preferences preferences;
    private UserModel userModel;
    private ImageView arrow_back;
    private LinearLayout ll_continue;
    private EditText edt_manufc, edt_responsible, edt_color, edt_platenume, edt_phone;
    //  private Spinner_Sub_catogry_Adapter spinner_sub_catogry_adapter;
    // private Spinner_catogry_Adapter spinner_catogry_adapter;
    //private Spinner_Sub_Sub_catogry_Adapter spinner_sub_sub_catogry_adapter;
    // private Spinner sp_cat, sp_sub_cat, sp_model;
    private Spinner cities;
    private Spinner_Adapter city_adapter;
    private List<CityModel> cities_models;
    private LinearLayout ll_cv;
    private RecyclerView recyclerView_images;
    private RadioGroup group_type;

    private GalleryAdapter galleryAdapter;
    private List<Uri> uriList;
    //private List<Catogry_Model.Categories.sub> subs;
    //private List<Catogry_Model.Categories> categories;
    //private List<Catogry_Model.Categories.sub.Sub> subs_sub;
    private final int IMG2 = 2;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    // private String  cat_id, sub_id, model_id;
    private String city_id, car_types;
    // private Adversiment_Model adversiment_model;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_car, container, false);
        initView(view);
        getCities();
        //    categories();
        return view;
    }

    private void initView(View view) {
        //adversiment_model = new Adversiment_Model();
        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        ll_continue = view.findViewById(R.id.ll_continue);
        group_type = view.findViewById(R.id.group_type);

        edt_manufc = view.findViewById(R.id.edt_manufacture);
        edt_responsible = view.findViewById(R.id.edt_responsilbel);
        edt_color = view.findViewById(R.id.edt_color);
        edt_platenume = view.findViewById(R.id.edt_platenum);
        edt_phone = view.findViewById(R.id.edt_phone);
        //   sp_cat = view.findViewById(R.id.sp_cat);
        ll_cv = view.findViewById(R.id.ll_cv);
        recyclerView_images = view.findViewById(R.id.recView_images);

        // sp_sub_cat = view.findViewById(R.id.sp_sub);
        //sp_model = view.findViewById(R.id.sp_model);
        arrow_back = view.findViewById(R.id.arrow_back);
        if (cuurent_language.equals("en")) {

            arrow_back.setRotation(180);
        }
        arrow_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });
        cities_models = new ArrayList<>();
        //categories = new ArrayList<>();
        //subs = new ArrayList<>();
        //subs_sub = new ArrayList<>();
        uriList = new ArrayList<>();
        recyclerView_images.setDrawingCacheEnabled(true);
        recyclerView_images.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);
        recyclerView_images.setItemViewCacheSize(25);
        galleryAdapter = new GalleryAdapter(uriList, homeActivity, this);
        recyclerView_images.setLayoutManager(new LinearLayoutManager(homeActivity, LinearLayoutManager.HORIZONTAL, true));
        recyclerView_images.setAdapter(galleryAdapter);

        if (cuurent_language.equals("ar")) {
            cities_models.add(new CityModel("مدينتى"));
            //  subs.add(new Catogry_Model.Categories.sub("النوع"));
            //categories.add(new Catogry_Model.Categories("كل الاقسام"));
            //subs_sub.add(new Catogry_Model.Categories.sub.Sub("الموديل"));

        } else {
            cities_models.add(new CityModel("City"));
            //subs.add(new Catogry_Model.Categories.sub("Type"));
            //categories.add(new Catogry_Model.Categories("all departments"));
            //subs_sub.add(new Catogry_Model.Categories.sub.Sub("model"));


        }
        if (group_type.getCheckedRadioButtonId() == R.id.r_new) {
            car_types = "1";
        } else if (group_type.getCheckedRadioButtonId() == R.id.r_used) {
            car_types = "2";
        }

        cities = view.findViewById(R.id.sp_city);
       /* spinner_catogry_adapter = new Spinner_catogry_Adapter(homeActivity, categories);
        spinner_sub_catogry_adapter = new Spinner_Sub_catogry_Adapter(homeActivity, subs);
        spinner_sub_sub_catogry_adapter = new Spinner_Sub_Sub_catogry_Adapter(homeActivity, subs_sub);
        sp_cat.setAdapter(spinner_catogry_adapter);
        sp_sub_cat.setAdapter(spinner_sub_catogry_adapter);
        sp_model.setAdapter(spinner_sub_sub_catogry_adapter);*/
        city_adapter = new Spinner_Adapter(homeActivity, cities_models);
        cities.setAdapter(city_adapter);

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
      /*  sp_cat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subs.clear();
                if (cuurent_language.equals("ar")) {
                    subs.add(new Catogry_Model.Categories.sub("النوع"));

                } else {
                    subs.add(new Catogry_Model.Categories.sub("Type"));

                }
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
        });*/
        ll_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel!=null){

                checkdata();}
                else {
                    Common.CreateUserNotSignInAlertDialog(homeActivity);

                }
            }


        });

    }

    private void checkdata() {
        Common.CloseKeyBoard(homeActivity,edt_phone);

        String manuf = edt_manufc.getText().toString();
        String responsilble = edt_responsible.getText().toString();
        String colors = edt_color.getText().toString();
        String platenum = edt_platenume.getText().toString();
        String phone = edt_phone.getText().toString();
        if (city_id != null && ((uriList.size() > 0)) && !TextUtils.isEmpty(manuf) && !TextUtils.isEmpty(responsilble) && !TextUtils.isEmpty(colors) && !TextUtils.isEmpty(platenum) && !TextUtils.isEmpty(phone)) {

            addlostcar(manuf, responsilble, colors, platenum, phone);

        } else {
            if (TextUtils.isEmpty(manuf)) {
                edt_manufc.setError(getResources().getString(R.string.field_req));
            }
            if (TextUtils.isEmpty(responsilble)) {
                edt_responsible.setError(getResources().getString(R.string.field_req));
            }

            if (TextUtils.isEmpty(colors)) {
                edt_color.setError(getResources().getString(R.string.field_req));
            }
            if (TextUtils.isEmpty(platenum)) {
                edt_platenume.setError(getResources().getString(R.string.field_req));
            }
            if (TextUtils.isEmpty(phone)) {
                edt_phone.setError(getResources().getString(R.string.field_req));
            }
         /*   if (city_id == null || cat_id == null || sub_id == null || (uriList.size() > 0)) {
                Common.CreateSignAlertDialog(homeActivity, getString(R.string.complete_all));

            }*/
        }
    }

    private void addlostcar(String manuf, String responsilble, String colors, String platenum, String phone) {
        final Dialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser_id());
//        RequestBody sub_part = Common.getRequestBodyText(sub_id);
        //      RequestBody model_part = Common.getRequestBodyText(model_id);
        RequestBody manuf_part = Common.getRequestBodyText(manuf);
        //RequestBody piece_part = Common.getRequestBodyText(adversiment_model.getPiece());

        RequestBody respons_part = Common.getRequestBodyText(responsilble);
        RequestBody plate_part = Common.getRequestBodyText(platenum);
        RequestBody city_part = Common.getRequestBodyText(city_id);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody color_part = Common.getRequestBodyText(colors);
RequestBody car_type=Common.getRequestBodyText(car_types);
        List<MultipartBody.Part> partImageList = getMultipartBodyList(uriList, "advertisement_images[]");
        Api.getService().addlostcar(user_part, manuf_part, respons_part, plate_part, city_part, phone_part, color_part,car_type, partImageList).enqueue(new Callback<Adversiting_Model>() {
            @Override
            public void onResponse(Call<Adversiting_Model> call, Response<Adversiting_Model> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {

                    // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                    homeActivity.Back();
                } else {
                    try {
                        Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.failed));
                        Log.e("Error", response.code() + "" + response.errorBody() + response.raw() + response.body() + response.headers());
                    }
                    catch (Exception e){

                    }


                }
            }

            @Override
            public void onFailure(Call<Adversiting_Model> call, Throwable t) {
                dialog.dismiss();
                try {
                    Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {

                }

            }
        });

    }


    public static Fragment_Add_Car newInstance() {
        return new Fragment_Add_Car();
    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(homeActivity, uri, image_cv);
            partList.add(part);
        }
        return partList;
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
/*
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
try {
    Log.e("Error", t.getMessage());

}
catch (Exception e){

}
                //progBar.setVisibility(View.GONE);
                //error.setText(activity.getString(R.string.faild));
                //mPager.setVisibility(View.GONE);
            }
        });
    }*/

    public void Delete(int position) {
        uriList.remove(position);
        galleryAdapter.notifyDataSetChanged();

    }

    private void Check_ReadPermission(int img_req) {
        if (ContextCompat.checkSelfPermission(homeActivity, read_permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(homeActivity, new String[]{read_permission}, img_req);
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
                Cursor cursor = homeActivity.getContentResolver().query(mImageUri,
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
                        Cursor cursor = homeActivity.getContentResolver().query(uri, filePathColumn, null, null, null);
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
                    Toast.makeText(homeActivity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


  /*  public void Deleteimageapi(String image_id) {
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
    }*/
}
