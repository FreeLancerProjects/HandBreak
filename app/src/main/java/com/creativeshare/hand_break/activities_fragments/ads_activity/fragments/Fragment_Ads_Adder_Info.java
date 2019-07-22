package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.models.Adversiment_Model;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Ads_Adder_Info extends Fragment {
    private AdsActivity adsActivity;
    private String cuurent_language;
    private Preferences preferences;
    private UserModel userModel;
    private CountryCodePicker ccp_phone;
    private EditText edt_title, edt_phone, edt_price, edt_desc;
    private Button bt_add;
    private Adversiment_Model adversiment_model;
    private String adversiment_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_adder_info, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        adversiment_id = Adversiment_Model.getId();
        adsActivity = (AdsActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(adsActivity);
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        edt_title = view.findViewById(R.id.edt_title);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_price = view.findViewById(R.id.edt_price);
        edt_desc = view.findViewById(R.id.edt_desc);
        ccp_phone = view.findViewById(R.id.ccp);
        bt_add = view.findViewById(R.id.bt_add);
        ccp_phone.registerCarrierNumberEditText(edt_phone);
        if (!adversiment_id.equals("-1")) {
            bt_add.setText(getResources().getString(R.string.edit));
        }
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edt_title.getText().toString();
                String phone = edt_phone.getText().toString();
                String price = edt_price.getText().toString();
                String desc = edt_desc.getText().toString();
                if (title.isEmpty() || phone.isEmpty() || !ccp_phone.isValidFullNumber() || desc.isEmpty()) {
                    if (title.isEmpty()) {
                        edt_title.setError(getResources().getString(R.string.field_req));
                    }
                    if (phone.isEmpty() || !ccp_phone.isValidFullNumber()) {
                        edt_phone.setError("");
                    }
                    if (desc.isEmpty()) {
                        edt_desc.setError(getResources().getString(R.string.field_req));

                    }
                } else {
                    if (adversiment_id.equals("-1")) {
                        createads(title, ccp_phone.getSelectedCountryCode().replace("+", "00") + phone, price, desc);
                    } else {
                        updateeads(title, ccp_phone.getSelectedCountryCode().replace("+", "00") + phone, price, desc, adversiment_id);

                    }
                }
            }
        });
    }

    public void setdata() {
        Adversiting_Model adversiting_model = Adversiment_Model.getAdversiting_model();
        if (adversiting_model != null) {
            edt_title.setText(adversiting_model.getAdvertisement_title());
            ccp_phone.setFullNumber(adversiting_model.getPhone());
            edt_phone.setText(ccp_phone.getFullNumber());
            edt_desc.setText(adversiting_model.getAdvertisement_content());
            edt_price.setText(adversiting_model.getAdvertisement_price());
        }
    }

    private void updateeads(String title, String phone, String price, String desc, String adversiment_id) {

        final Dialog dialog = Common.createProgressDialog(adsActivity, getString(R.string.wait));
        dialog.show();
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser_id());
        RequestBody cat_part = Common.getRequestBodyText(adversiment_model.getCat_id());
        RequestBody sub_part = Common.getRequestBodyText(adversiment_model.getSub_id());
        RequestBody model_part = Common.getRequestBodyText(adversiment_model.getModel_id());
        RequestBody city_part = Common.getRequestBodyText(adversiment_model.getCity_id());
        RequestBody title_part = Common.getRequestBodyText(title);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody price_part = Common.getRequestBodyText(price);
        RequestBody desc_part = Common.getRequestBodyText(desc);
        RequestBody adversiment_part = Common.getRequestBodyText(adversiment_id);

        List<MultipartBody.Part> partImageList = getMultipartBodyList(adversiment_model.getUris(), "advertisement_images[]");
        Api.getService().updateads(user_part, cat_part, sub_part, model_part, title_part, desc_part, price_part, city_part, phone_part, adversiment_part, partImageList).enqueue(new Callback<Adversiting_Model>() {
            @Override
            public void onResponse(Call<Adversiting_Model> call, Response<Adversiting_Model> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));
                    adsActivity.finish();
                } else {
                    Common.CreateSignAlertDialog(adsActivity, getResources().getString(R.string.failed));
                    Log.e("Error", response.code() + "" + response.errorBody() + response.raw() + response.body() + response.headers());

                }
            }

            @Override
            public void onFailure(Call<Adversiting_Model> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(adsActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });

    }

    private void createads(String title, String phone, String price, String desc) {

        final Dialog dialog = Common.createProgressDialog(adsActivity, getString(R.string.wait));
        dialog.show();
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser_id());
        RequestBody cat_part = Common.getRequestBodyText(adversiment_model.getCat_id());
        RequestBody sub_part = Common.getRequestBodyText(adversiment_model.getSub_id());
        RequestBody model_part = Common.getRequestBodyText(adversiment_model.getModel_id());
        RequestBody city_part = Common.getRequestBodyText(adversiment_model.getCity_id());
        RequestBody title_part = Common.getRequestBodyText(title);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody price_part = Common.getRequestBodyText(price);
        RequestBody desc_part = Common.getRequestBodyText(desc);
        List<MultipartBody.Part> partImageList = getMultipartBodyList(adversiment_model.getUris(), "advertisement_images[]");
        Api.getService().addads(user_part, cat_part, sub_part, model_part, title_part, desc_part, price_part, city_part, phone_part, partImageList).enqueue(new Callback<Catogry_Model.Advertsing>() {
            @Override
            public void onResponse(Call<Catogry_Model.Advertsing> call, Response<Catogry_Model.Advertsing> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Common.CreateSignAlertDialog(adsActivity, getResources().getString(R.string.suc));
                    adsActivity.finish();
                } else {
                    Common.CreateSignAlertDialog(adsActivity, getResources().getString(R.string.failed));

                }
            }

            @Override
            public void onFailure(Call<Catogry_Model.Advertsing> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(adsActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });

    }

    public static Fragment_Ads_Adder_Info newInstance() {
        return new Fragment_Ads_Adder_Info();
    }


    public void setmodel(Adversiment_Model adversiment_model) {
        this.adversiment_model = adversiment_model;

    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(adsActivity, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }
}