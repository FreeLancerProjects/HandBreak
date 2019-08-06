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
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Spinner_Adapter;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.creativeshare.hand_break.tags.Tags;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Edit_Profile extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    // private CircleImageView imageprofile;
    private CountryCodePicker countryCodePicker;
    private EditText edt_name, edt_email, edt_phone, edt_location, edt_address,  edt_pass;
    private Spinner cities;
    private String city_id="all";
    private Preferences preferences;
    private UserModel userModel;
    private Spinner_Adapter city_adapter;
    private List<CityModel> cities_models;
    private Button bt_save;
    private CircleImageView image;
    private final int IMG1 = 1;
    private Uri uri = null;
    private ImageView back;
    private final String read_permission = Manifest.permission.READ_EXTERNAL_STORAGE;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initView(view);
        getCities();
        return view;
    }

    private void initView(View view) {
        cities_models = new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        cities = view.findViewById(R.id.sp_city);
        //   imageprofile = view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_phone = view.findViewById(R.id.edt_phone);
        image = view.findViewById(R.id.image);
        back = view.findViewById(R.id.arrow_back);

        // edt_location = view.findViewById(R.id.edt_loc);
        edt_address = view.findViewById(R.id.edt_address);
       // edt_commercial = view.findViewById(R.id.edt_commercial);
        countryCodePicker = view.findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(edt_phone);
        edt_pass = view.findViewById(R.id.edt_password);
        bt_save = view.findViewById(R.id.bt_save);
        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }
        if (cuurent_language.equals("ar")) {
            cities_models.add(new CityModel("مدينتى"));
            //   subs.add(new Catogry_Model.Categories.sub("الكل"));

        } else {
            cities_models.add(new CityModel("City"));
            // subs.add(new Catogry_Model.Categories.sub("all"));

        }

        city_adapter = new Spinner_Adapter(homeActivity, cities_models);
        cities.setAdapter(city_adapter);
        cities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    city_id = cities_models.get(i).getId_city();
                } else {
                    city_id = "all";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        updateprofile();
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Check_ReadPermission(IMG1);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });

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
        if (requestCode == IMG1 && resultCode == Activity.RESULT_OK && data != null) {
            uri = data.getData();

            UpdateImage(uri);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG1) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    select_photo(IMG1);
                } else {
                    Toast.makeText(homeActivity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void UpdateImage(Uri uri) {

        final Dialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.show();

        RequestBody token_part = Common.getRequestBodyText(userModel.getUser_id());


        try {
            MultipartBody.Part avatar_part = Common.getMultiPart(homeActivity, uri, "user_image");
            Api.getService()
                    .udateimage(token_part, avatar_part)
                    .enqueue(new Callback<UserModel>() {
                        @Override
                        public void onResponse(Call<UserModel> call, Response<UserModel> response) {

                            dialog.dismiss();

                            if (response.isSuccessful()) {

                                if (response.body() != null) {
                                    Toast.makeText(homeActivity, getString(R.string.suc), Toast.LENGTH_SHORT).show();
                                    userModel = response.body();
                                    preferences.create_update_userdata(homeActivity,userModel);
                                    updateprofile();

                                }

                            } else {
                                Common.CreateSignAlertDialog(homeActivity, getString(R.string.something));
                            }

                        }

                        @Override
                        public void onFailure (Call < UserModel > call, Throwable t){
                            try {
                                dialog.dismiss();
                                Log.e("Error", t.getMessage());
                            } catch (Exception e) {
                            }
                        }
                    });
        } catch(
                Exception e)

        {
            Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();

        }

    }

    private void checkdata() {
        String name = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String phone = edt_phone.getText().toString();
        String phonecode = countryCodePicker.getSelectedCountryCode();
        //String city = edt_location.getText().toString();
        String address = edt_address.getText().toString();

        //String coomericial = edt_commercial.getText().toString();
        String pass = edt_pass.getText().toString();
        if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || phone.isEmpty() || !countryCodePicker.isValidFullNumber() || pass.isEmpty() || pass.length() < 6 || address.isEmpty() || city_id.equals("all")) {
            if (name.isEmpty()) {
name=userModel.getUser_name();
            }
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
email=userModel.getUser_email();            }
            if (phone.isEmpty() || !countryCodePicker.isValidFullNumber()) {
phone=userModel.getUser_phone();
            phonecode=userModel.getUser_phone_code();

            }

            if (address.isEmpty()) {
                if(userModel.getUser_address()!=null){
address=userModel.getUser_address()+"";            }
            else {
                address="0";
                }
            }
            if (city_id.equals("all")) {
                if(userModel.getUser_city()!=null){
city_id=userModel.getUser_city()+"";}

            }
        }

         if (pass.isEmpty() || pass.length() < 6) {
             edt_pass.setError("");
        }
        else {
            updateprofile(name, email, phone, phonecode, address, pass, city_id);}

    }

    private void updateprofile(String name, String email, String phone, String phonecode, final String address, String pass, final String city_id) {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().updateprofile(userModel.getUser_id(), email, name, phone, phonecode.replace("+", "00"), address + "", pass, city_id).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    preferences = Preferences.getInstance();
                    preferences.create_update_userdata(homeActivity, response.body());
                   // Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.suc));
                    homeActivity.Back();
                    homeActivity.Back();
                    homeActivity.DisplayFragmentProfile();
                   // edt_pass.setText("");
                   // updateprofile();
                } else {

                    try {
                        Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                try {
                    Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
                catch (Exception e){

                }

            }
        });
    }

    private void updateprofile() {
        userModel = preferences.getUserData(homeActivity);
        if (userModel != null) {
            if (userModel.getUser_image() != null && !userModel.getUser_image().equals("0")) {
                Picasso.with(homeActivity).load(Tags.IMAGE_URL + userModel.getUser_image()).fit().into(image);
            }
            if (userModel.getUser_name() != null) {
                edt_name.setText(userModel.getUser_name());
            }
            if (userModel.getUser_city() != null) {
//                edt_location.setText(userModel.getUser_city());
            }
            if (userModel.getUser_address() != null) {
                edt_address.setText(userModel.getUser_address());
            }

            if (userModel.getUser_phone() != null) {
                edt_phone.setText(userModel.getUser_phone());
                countryCodePicker.setCountryForPhoneCode(Integer.parseInt(userModel.getUser_phone_code()));
            }
            if (userModel.getUser_email() != null) {
                edt_email.setText(userModel.getUser_email());
            }
            if (userModel.getUser_address() != null) {
                edt_address.setText(userModel.getUser_address());
            }
            if (userModel.getCommercial_register() != null) {
                userModel.getCommercial_register();
            }
            // edt_pass.setText(userModel.get);

        }
    }

    public static Fragment_Edit_Profile newInstance() {
        return new Fragment_Edit_Profile();
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

}
