package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.renderscript.Float2;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.Adversiment_Model;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.creativeshare.hand_break.tags.Tags;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Profile extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private CircleImageView imageprofile;
    private TextView tv_name, tv_loaction, tv_address, tv_phone, tv_email;
    private ImageView arrow1, arrow2, arrow3, arrow4, arrow5, im_edit;
    private LinearLayout lll;
    private SwitchCompat switchCompat;
    private SimpleRatingBar simpleRatingBar;
    private Button bt_upgrade;
    private Preferences preferences;
    private UserModel userModel;
    private ImageView back;
    List<CityModel> cityModels;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        getCities();
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();

        if (Adversiment_Model.getId() == null) {
            userModel = preferences.getUserData(homeActivity);
        }

        cityModels = new ArrayList<>();
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        imageprofile = view.findViewById(R.id.image);
        tv_name = view.findViewById(R.id.tv_name);
        tv_loaction = view.findViewById(R.id.tv_location);
        tv_address = view.findViewById(R.id.tv_address);
        //tv_commericial = view.findViewById(R.id.tv_commercial);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_email = view.findViewById(R.id.tv_email);
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        im_edit = view.findViewById(R.id.im_edit);
        back = view.findViewById(R.id.arrow_back);
        simpleRatingBar = view.findViewById(R.id.rating);
        bt_upgrade = view.findViewById(R.id.bt_upgrade);
        lll=view.findViewById(R.id.ll);
        switchCompat=view.findViewById(R.id.switch1);
        if (cuurent_language.equals("en")) {
            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
            back.setRotation(180);

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.Back();
            }
        });
        bt_upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentupgrade();
            }
        });
        if (Adversiment_Model.getId() != null) {
            im_edit.setImageDrawable(getResources().getDrawable(R.drawable.follow));
            simpleRatingBar.setVisibility(View.VISIBLE);
            bt_upgrade.setVisibility(View.GONE);
            lll.setVisibility(View.GONE);

        } else {
            if (userModel != null) {
                if (userModel.getUser_type().equals("2")) {

                    bt_upgrade.setVisibility(View.GONE);
                }

            }
        }

        //  updateprofile();
        im_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Adversiment_Model.getId() == null) {
                    homeActivity.DisplayFragmentEditProfile();
                } else {
                    followuser();
                }
            }
        });
        if (Adversiment_Model.getId() != null) {
            simpleRatingBar.setIndicator(true);
        }
        switchCompat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null) {
                    changenotifystatus();
                }
            }

        });
        simpleRatingBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel != null && Adversiment_Model.getId() != null) {
                    if (userModel.isRating_status() == false) {
                        updaterating();
                    }
                }
            }
        });
    }


        private void changenotifystatus() {
            final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
            dialog.setCancelable(false);
            dialog.show();
           // Log.e("user", userModel.getToken());
            String insurancetype;
            if(switchCompat.isChecked()){
                insurancetype="0";
            }
            else {
                insurancetype="1";
            }
            Api.getService().updateprofile(userModel.getUser_id(),insurancetype).enqueue(new Callback<UserModel>() {
                @Override
                public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        preferences.create_update_userdata(homeActivity,response.body());
                        userModel=preferences.getUserData(homeActivity);
                        updateprofile();
                    } else {

                        Log.e("error_code", response.code() + "_" + response.errorBody() + response.message() + response.raw() + response.headers());


                        Toast.makeText(homeActivity, getString(R.string.failed), Toast.LENGTH_SHORT).show();

                    }
                }

                @Override
                public void onFailure(Call<UserModel> call, Throwable t) {
                    dialog.dismiss();
                    Log.e("Error", t.getMessage());
                    Toast.makeText(homeActivity, getResources().getString(R.string.something), Toast.LENGTH_LONG).show();
                }
            });
        }


    private void updaterating() {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().makerating(userModel.getUser_id(), preferences.getUserData(homeActivity).getUser_id(), simpleRatingBar.getRating() + "").enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //Log.e("uu",((float)simpleRatingBar.getRating())+""+userModel.getUser_id()+" "+preferences.getUserData(homeActivity).getUser_id());
                    userModel.setRating_value(simpleRatingBar.getRating());
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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();

                try {
                    Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {

                }
            }
        });
    }

    private void followuser() {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().followuser(preferences.getUserData(homeActivity).getUser_id(), userModel.getUser_id()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    if (userModel.isUser_follow() == true) {
                        im_edit.setImageDrawable(getResources().getDrawable(R.drawable.follow));
                        userModel.setUser_follow(false);

                    } else {
                        im_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
                        userModel.setUser_follow(true);

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
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();

                try {
                    Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {


                }
            }
        });
    }

    private void getdata(final String id) {
        final ProgressDialog dialog = Common.createProgressDialog(homeActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        ;
        Api.getService().Showotherprofile(Preferences.getInstance().getUserData(homeActivity).getUser_id(), id).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    userModel = response.body();
                    updateprofile();

                } else {
                    Toast.makeText(homeActivity, R.string.failed, Toast.LENGTH_SHORT).show();
                    Log.e("Error_code", response.code() + "" + response.errorBody() + response.headers() + response.message() + response.raw() + " " + id);
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                try {
                    dialog.dismiss();
                    Toast.makeText(homeActivity, R.string.something, Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
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
                                cityModels.clear();

                                cityModels.addAll(response.body());
                                if (Adversiment_Model.getId() == null) {
                                    updateprofile();
                                } else {
                                    getdata(Adversiment_Model.getId());
                                }


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

    private void updateprofile() {
        if (userModel != null) {
            if (userModel.getUser_image() != null && !userModel.getUser_image().equals("0")) {
                Picasso.with(homeActivity).load(Tags.IMAGE_URL + userModel.getUser_image()).fit().into(imageprofile);
            }
            if (userModel.getUser_name() != null) {
                tv_name.setText(userModel.getUser_name());
            }
            if (userModel.getUser_city() != null) {
                //  Log.e("msg",cityModels.size()+"");
                //  tv_loaction.setText(userModel.getUser_city());
                if (cuurent_language.equals("en")) {
                    tv_loaction.setText(userModel.getEn_city_title());
                } else {
                    tv_loaction.setText(userModel.getAr_city_title());
                }
            }
            if (userModel.getUser_address() != null) {
                tv_address.setText(userModel.getUser_address());
            }
            if (userModel.getCommercial_register() != null) {
                //tv_commericial.setText(userModel.getCommercial_register());
            }
            if (userModel.getUser_phone() != null) {
                tv_phone.setText(userModel.getUser_phone());
            }
            if (userModel.getUser_email() != null) {
                tv_email.setText(userModel.getUser_email());
            }
            if (Adversiment_Model.getId() != null) {
                if (userModel.isUser_follow() == true) {
                    im_edit.setImageDrawable(getResources().getDrawable(R.drawable.ic_follow));
                } else {
                    im_edit.setImageDrawable(getResources().getDrawable(R.drawable.follow));

                }

                Log.e("lll", userModel.getRating_value() + "");
                simpleRatingBar.setRating(userModel.getRating_value());
                if(userModel.getInsurance_services().equals("0")){
                    switchCompat.setChecked(false);
                }
                else {
                    switchCompat.setChecked(true);
                }
            }

        }
    }

    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

}
