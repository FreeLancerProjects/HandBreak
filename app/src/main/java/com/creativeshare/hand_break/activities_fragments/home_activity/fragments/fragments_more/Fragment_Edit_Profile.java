package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.creativeshare.hand_break.tags.Tags;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Edit_Profile extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private CircleImageView imageprofile;
    private CountryCodePicker countryCodePicker;
    private EditText edt_name, edt_email, edt_phone, edt_location, edt_address, edt_commercial, edt_pass;
    private Preferences preferences;
    private UserModel userModel;
    private Button bt_save;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        imageprofile = view.findViewById(R.id.image);
        edt_name = view.findViewById(R.id.edt_name);
        edt_email = view.findViewById(R.id.edt_email);
        edt_phone = view.findViewById(R.id.edt_phone);
        // edt_location = view.findViewById(R.id.edt_loc);
        edt_address = view.findViewById(R.id.edt_address);
        edt_commercial = view.findViewById(R.id.edt_commercial);
        countryCodePicker = view.findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(edt_phone);
        edt_pass = view.findViewById(R.id.edt_password);
        bt_save = view.findViewById(R.id.bt_save);
        updateprofile();
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkdata();
            }
        });


    }

    private void checkdata() {
        String name = edt_name.getText().toString();
        String email = edt_email.getText().toString();
        String phone = edt_phone.getText().toString();
        String phonecode = countryCodePicker.getSelectedCountryCode();
        //String city = edt_location.getText().toString();
        String address = edt_address.getText().toString();
        String coomericial = edt_commercial.getText().toString();
        String pass = edt_pass.getText().toString();
        if (name.isEmpty() || email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches() || phone.isEmpty() || !countryCodePicker.isValidFullNumber() || pass.isEmpty() || pass.length() < 6) {
            if (name.isEmpty()) {
                edt_name.setError("");
            }
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                edt_email.setError("");
            }
            if (phone.isEmpty() || !countryCodePicker.isValidFullNumber()) {
                edt_phone.setError("");
            }
            if (pass.isEmpty() || pass.length() < 6) {
                edt_pass.setError("");
            }
        } else {
            updateprofile(name, email, phone, phonecode, address, coomericial, pass);
        }
    }

    private void updateprofile(String name, String email, String phone, String phonecode, final String address, String coomericial, String pass) {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().updateprofile(userModel.getUser_id(), email, name, phone, phonecode.replace("+", "00"), address, coomericial, pass).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    preferences = Preferences.getInstance();
                    preferences.create_update_userdata(homeActivity, response.body());
                    Common.CreateSignAlertDialog(homeActivity, getResources().getString(R.string.suc));
                    edt_pass.setText("");
                } else {

                    try {
                        Log.e("Error_code", response.code() + "_" + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                Log.e("Error", t.getMessage());
            }
        });
    }

    private void updateprofile() {
        if (userModel != null) {
            if (userModel.getUser_image() != null && !userModel.getUser_image().equals("0")) {
                Picasso.with(homeActivity).load(Tags.IMAGE_URL + userModel.getUser_image()).fit().into(imageprofile);
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
            if (userModel.getCommercial_register() != null) {
                edt_commercial.setText(userModel.getCommercial_register());
            }
            if (userModel.getUser_phone() != null) {
                edt_phone.setText(userModel.getUser_phone());
                countryCodePicker.setCountryForPhoneCode(Integer.parseInt(userModel.getUser_phone_code()));
            }
            // edt_pass.setText(userModel.get);

        }
    }

    public static Fragment_Edit_Profile newInstance() {
        return new Fragment_Edit_Profile();
    }

}
