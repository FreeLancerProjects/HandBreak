package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_insurance_cars;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.Insuarce_Model;
import com.creativeshare.hand_break.models.Notification_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.creativeshare.hand_break.tags.Tags;
import com.hbb20.CountryCodePicker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Send_insurance_Offer extends Fragment {

    private HomeActivity activity;
    private String current_language;
    private final static String Tag1 = "order";
    private Notification_Model.Data data;
    private ImageView back_arrow;
    private Preferences preferences;
    private UserModel userModel;
    private Button bt_send;
    private EditText edt_offer;
    private TextView tv_phone, tv_ownername, tv_idnum, tv_time, tv_model, tv_type;
    private RoundedImageView imagecar, imageform;

    public static Fragment_Send_insurance_Offer newInstance(Notification_Model.Data data) {
        Fragment_Send_insurance_Offer fragment_send_insurance_offer = new Fragment_Send_insurance_Offer();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Tag1, data);
        fragment_send_insurance_offer.setArguments(bundle);
        return fragment_send_insurance_offer;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_insurance_offer, container, false);
        initView(view);
        getioneinsurance();
        return view;
    }

    private void getioneinsurance() {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().showoneinsurancerequsts(userModel.getUser_id(), data.getRequest_id()).enqueue(new Callback<Insuarce_Model>() {
            @Override
            public void onResponse(Call<Insuarce_Model> call, Response<Insuarce_Model> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    updatedata(response.body());
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                    Log.e("Error_Code", response.code() + "  " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Insuarce_Model> call, Throwable t) {
                dialog.dismiss();
                try {
                    Log.e("Error", t.getMessage());
                    Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();
                }
                catch (Exception e){

                }
            }
        });
    }

    private void updatedata(Insuarce_Model body) {
        tv_phone.setText(body.getCar_owner_phone());
        tv_ownername.setText(body.getCar_owner_name());
        tv_idnum.setText(body.getPersonal_id_num());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.ENGLISH);
        String date = dateFormat.format(new Date(Long.parseLong(body.getDate()) * 1000));
        tv_time.setText(date);
        tv_model.setText(body.getCar_model());
        tv_type.setText(body.getCar_type());
        Picasso.with(activity).load(Tags.IMAGE_URL + body.getCar_image()).fit().into(imagecar);
        Picasso.with(activity).load(Tags.IMAGE_URL + body.getForm_image()).fit().into(imageform);
    }


    private void initView(View view) {
        data = (Notification_Model.Data) getArguments().getSerializable(Tag1);
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);

        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        back_arrow = view.findViewById(R.id.arrow);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_ownername = view.findViewById(R.id.tv_owner_name);
        tv_idnum = view.findViewById(R.id.tv_idnume);
        tv_time = view.findViewById(R.id.tv_time);
        tv_model = view.findViewById(R.id.tv_model);
        tv_type = view.findViewById(R.id.tv_type);
        imagecar = view.findViewById(R.id.image_car);
        imageform = view.findViewById(R.id.image_form);
        edt_offer = view.findViewById(R.id.edt_offer);
        bt_send = view.findViewById(R.id.btn_send);
        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.Back();
            }
        });
        if (current_language.equals("ar")) {
            back_arrow.setRotation(180.0f);
        }
        bt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });

    }

    private void checkdata() {
        Common.CloseKeyBoard(activity,edt_offer);

        String offer = edt_offer.getText().toString();
        if (TextUtils.isEmpty(offer)) {
            edt_offer.setError(getResources().getString(R.string.field_req));
        } else {
            sendoffer(offer);
        }
    }

    private void sendoffer(String offer) {
        final Dialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().sendinsuranceoffer(userModel.getUser_id(), data.getFrom_user_id_fk(), data.getId_notification(), data.getRequest_id(), offer).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    Toast.makeText(activity, getResources().getString(R.string.suc), Toast.LENGTH_LONG).show();
                    activity.refresh();
                    activity.Back();
                } else {
                    Toast.makeText(activity, getResources().getString(R.string.failed), Toast.LENGTH_LONG).show();
                    Log.e("Error_Code", response.code() + "  " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                try {
                    Log.e("Error", t.getMessage());
                    Toast.makeText(activity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();
                }
                catch (Exception e){

                }

            }
        });
    }


}





