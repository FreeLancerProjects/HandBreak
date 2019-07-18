package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.tags.Tags;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Fragment_Edit_Profile extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private CircleImageView imageprofile;
    private CountryCodePicker countryCodePicker;
    private EditText edt_name, edt_email, edt_phone, edt_location, edt_address, edt_commercial;
    private Preferences preferences;
    private UserModel userModel;

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
        edt_location = view.findViewById(R.id.edt_loc);
        edt_address = view.findViewById(R.id.edt_address);
        edt_commercial = view.findViewById(R.id.edt_commercial);
        countryCodePicker = view.findViewById(R.id.ccp);
        updateprofile();

    }

    private void updateprofile() {
        if (userModel != null) {
            if (userModel.getUser_image() != null&&!userModel.getUser_image().equals("0")) {
                Picasso.with(homeActivity).load(Tags.IMAGE_URL + userModel.getUser_image()).fit().into(imageprofile);
            }
            if (userModel.getUser_name() != null) {
                 edt_name.setText(userModel.getUser_name());
            }
            if (userModel.getUser_city() != null) {
                  edt_location.setText(userModel.getUser_city());
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
            if (userModel.getUser_email() != null) {
                edt_email.setText(userModel.getUser_email());
            }

        }
    }

    public static Fragment_Edit_Profile newInstance() {
        return new Fragment_Edit_Profile();
    }

}
