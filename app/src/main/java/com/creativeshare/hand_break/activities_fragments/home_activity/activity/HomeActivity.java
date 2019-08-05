package com.creativeshare.hand_break.activities_fragments.home_activity.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Car_Search;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Home;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Main;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Search;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_insurance_cars.Fragment_Accept_Refues_insurance_Offer;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_insurance_cars.Fragment_Send_insurance_Offer;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_insurance_cars.Fragment_insurance_car;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_About;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Add_Car;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Adversiment_Detials;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_App_Percentage;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Edit_Profile;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Follower_List;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_More;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_My_adversiment;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Profile;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Terms_Conditions;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Upgrade;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_msg_notfy.Fragment_Message_Notifications;
import com.creativeshare.hand_break.activities_fragments.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.hand_break.language.Language_Helper;
import com.creativeshare.hand_break.models.Notification_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.creativeshare.hand_break.tags.Tags;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private int fragment_count = 0;
    private Fragment_Home fragment_home;
    private Fragment_Main fragment_main;
    private Fragment_Message_Notifications fragment_message_notifications;
    private Fragment_Search fragment_search;
    private Fragment_Car_Search fragment_car_search;
    private Fragment_More fragment_more;
    private Fragment_Follower_List fragment_follower_list;
    private Fragment_Terms_Conditions fragmentTerms_conditions;
    private Fragment_About fragment_about;
    private Fragment_Profile fragment_profile;
    private Fragment_Edit_Profile fragment_edit_profile;
    private Fragment_App_Percentage fragment_app_percentage;
    private Fragment_My_adversiment fragment_my_adversiment;
    private Fragment_Add_Car fragment_add_car;
    private Fragment_Adversiment_Detials fragment_adversiment_detials;
    private Fragment_insurance_car fragment_insurance_car;
    private Fragment_Send_insurance_Offer fragment_send_insurance_offer;
    private Fragment_Accept_Refues_insurance_Offer fragment_accept_refues_insurance_offer;
    private Fragment_Upgrade fragment_upgrade;
    private String cuurent_language;
    private Preferences preferences;
    private UserModel userModel;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase, Paper.book().read("lang", Locale.getDefault().getLanguage())));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        if (savedInstanceState == null) {
            CheckPermission();

            DisplayFragmentHome();
            DisplayFragmentMain();
        }

        if (userModel != null) {
            updateToken();
        }

    }
    private void CheckPermission()
    {
        if (ActivityCompat.checkSelfPermission(this, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{gps_perm}, gps_req);
        } else {

           // initGoogleApiClient();
           /* if (isGpsOpen())
            {
                StartService(LocationRequest.PRIORITY_HIGH_ACCURACY);
            }else
                {
                    CreateGpsDialog();

                }*/
        }
    }

    private void initView() {
        Paper.init(this);
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentManager = this.getSupportFragmentManager();
        String visitTime = preferences.getVisitTime(this);
        Calendar calendar = Calendar.getInstance();
        long timeNow = calendar.getTimeInMillis();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        String date = dateFormat.format(new Date(timeNow));

        if (!date.equals(visitTime)) {
            addVisit(date);
        }
    }

    private void updateToken() {
        FirebaseInstanceId.getInstance()
                .getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (task.isSuccessful()) {
                            String token = task.getResult().getToken();
                            Api.getService()
                                    .updateToken(userModel.getUser_id(), token)
                                    .enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            if (response.isSuccessful()) {
                                                Log.e("Success", "token updated");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                                            try {
                                                Log.e("Error", t.getMessage());
                                            } catch (Exception e) {
                                            }
                                        }
                                    });
                        }
                    }
                });
    }

    public void DisplayFragmentHome() {
        fragment_count += 1;
        if (fragment_home == null) {
            fragment_home = Fragment_Home.newInstance();
        }

        if (fragment_home.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_home).commit();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_home, "fragment_home").addToBackStack("fragment_home").commit();

        }

    }

    public void DisplayFragmentMain() {
        if (fragment_main == null) {
            fragment_main = Fragment_Main.newInstance();
        }
        if (fragment_message_notifications != null && fragment_message_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_message_notifications).commit();
        }
        if (fragment_search != null && fragment_search.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_search).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_main.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_main).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(0);
            //fragment_home.setsub();
        }

    }

    public void DisplayFragmentTerms_Condition() {

        fragment_count += 1;
        fragmentTerms_conditions = Fragment_Terms_Conditions.newInstance();


        if (fragmentTerms_conditions.isAdded()) {
            fragmentManager.beginTransaction().show(fragmentTerms_conditions).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragmentTerms_conditions, "fragmentTerms_conditions").addToBackStack("fragmentTerms_conditions").commit();

        }


    }

    public void DisplayFragmentAbout() {

        fragment_count += 1;
        fragment_about = Fragment_About.newInstance();


        if (fragment_about.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_about).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_about, "fragment_about").addToBackStack("fragment_about").commit();

        }


    }
    public void DisplayFragmentCarSearch() {

        fragment_count += 1;
        fragment_car_search= Fragment_Car_Search.newInstance();


        if (fragment_car_search.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_car_search).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_car_search, "fragment_car_search").addToBackStack("fragment_car_search").commit();

        }


    }
    public void DisplayFragmentMYAdversiment() {

        fragment_count += 1;
        fragment_my_adversiment = Fragment_My_adversiment.newInstance();


        if (fragment_my_adversiment.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_my_adversiment).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_my_adversiment, "fragment_my_adversiment").addToBackStack("fragment_my_adversiment").commit();

        }

    }
    public void DisplayFragmentfollowers() {

        fragment_count += 1;
        fragment_follower_list = Fragment_Follower_List.newInstance();


        if (fragment_follower_list.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_follower_list).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_follower_list, "fragment_follower_list").addToBackStack("fragment_follower_list").commit();

        }

    }
    public void DisplayFragmentAdversimentDetials(String id_adversiment) {
        fragment_count += 1;
        fragment_adversiment_detials = Fragment_Adversiment_Detials.newInstance(id_adversiment);


        if (fragment_adversiment_detials.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_adversiment_detials).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_adversiment_detials, "fragment_adversiment_detials").addToBackStack("fragment_adversiment_detials").commit();

        }
    }
    public void DisplayFragmentupgrade() {
        fragment_count += 1;
        fragment_upgrade = Fragment_Upgrade.newInstance();


        if (fragment_upgrade.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_upgrade).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_upgrade, "fragment_upgrade").addToBackStack("fragment_upgrade").commit();

        }
    }
    public void DisplayFragmentProfile() {
        fragment_count += 1;
        fragment_profile = Fragment_Profile.newInstance();


        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
    }

    public void DisplayFragmentEditProfile() {
        fragment_count += 1;
        fragment_edit_profile = Fragment_Edit_Profile.newInstance();


        if (fragment_edit_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_edit_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_edit_profile, "fragment_edit_profile").addToBackStack("fragment_edit_profile").commit();

        }
    }
    public void DisplayFragmentAddCar() {
        fragment_count += 1;
        fragment_add_car = Fragment_Add_Car.newInstance();


        if (fragment_add_car.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_add_car).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_add_car, "fragment_add_car").addToBackStack("fragment_add_car").commit();

        }
    }
    public void DisplayFragmentInsuranceCar() {
        fragment_count += 1;
        fragment_insurance_car = Fragment_insurance_car.newInstance();


        if (fragment_insurance_car.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_insurance_car).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_insurance_car, "fragment_insurance_car").addToBackStack("fragment_insurance_car").commit();

        }
    }
    public void DisplayFragmentSendInsuranceOffer(Notification_Model.Data data) {
        fragment_count += 1;
        fragment_send_insurance_offer = Fragment_Send_insurance_Offer.newInstance(data);


        if (fragment_send_insurance_offer.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_send_insurance_offer).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_send_insurance_offer, "fragment_send_insurance_offer").addToBackStack("fragment_send_insurance_offer").commit();

        }
    }
    public void DisplayFragmentAccept_RefuesInsuranceOffer(Notification_Model.Data data) {
        fragment_count += 1;
        fragment_accept_refues_insurance_offer = Fragment_Accept_Refues_insurance_Offer.newInstance(data);


        if (fragment_accept_refues_insurance_offer.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_accept_refues_insurance_offer).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_accept_refues_insurance_offer, "fragment_accept_refues_insurance_offer").addToBackStack("fragment_accept_refues_insurance_offer").commit();

        }
    }
    public void DisplayFragmentApp_percentage() {
        fragment_count += 1;
        fragment_app_percentage = Fragment_App_Percentage.newInstance();


        if (fragment_app_percentage.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_app_percentage).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_app_percentage, "fragment_app_percentage").addToBackStack("fragment_app_percentage").commit();

        }
    }
    public void DisplayFragmentnotifications() {
        if (fragment_message_notifications == null) {
            fragment_message_notifications = Fragment_Message_Notifications.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_search != null && fragment_search.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_search).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_message_notifications.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_message_notifications).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_message_notifications, "fragment_message_notifications").addToBackStack("fragment_message_notifications").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(1);
        }
    }

    public void DisplayFragmentSearch() {
        if (fragment_search == null) {
            fragment_search = Fragment_Search.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_message_notifications != null && fragment_message_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_message_notifications).commit();
        }
        if (fragment_more != null && fragment_more.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }

        if (fragment_search.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_search).commit();
            fragment_search.changevisible();

        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_search, "fragment_search").addToBackStack("fragment_search").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(2);
        }
    }


    public void DisplayFragmentMore() {
        if (fragment_more == null) {
            fragment_more = Fragment_More.newInstance();
        }
        if (fragment_main != null && fragment_main.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if (fragment_search != null && fragment_search.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_search).commit();
        }
        if (fragment_message_notifications != null && fragment_message_notifications.isAdded()) {
            fragmentManager.beginTransaction().hide(fragment_message_notifications).commit();
        }

        if (fragment_more.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_more).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }
        if (fragment_home != null && fragment_home.isAdded()) {
            fragment_home.updateBottomNavigationPosition(3);
        }
    }


    public void getoAds(String adversiment_id) {
        Intent intent = new Intent(HomeActivity.this, AdsActivity.class);
        intent.putExtra("adversiment_id", adversiment_id);

        startActivityForResult(intent,1);
    }


    @Override
    public void onBackPressed() {
        Back();
    }

    public void Back() {
        if (fragment_count > 1) {
            fragment_count -= 1;
            super.onBackPressed();
        } else {

            if (fragment_home != null && fragment_home.isVisible()) {
                if (fragment_main != null && fragment_main.isVisible()) {
                    if (userModel == null) {
            Common.CreateUserNotSignInAlertDialog(this);
                    } else {
                        finish();
                    }
                } else {
                    DisplayFragmentMain();
                }
            } else {
                DisplayFragmentHome();
            }
        }

    }
    public void RefreshActivity(String lang)
    {
        Paper.book().write("lang",lang);
        preferences.create_update_language(this,lang);
        Language_Helper.setNewLocale(this,lang);
        new Handler()
                .postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Intent intent =  getIntent();
                        finish();
                        startActivity(intent);
                    }
                },1050);



    }
    public void NavigateToSignInActivity(boolean isSignIn) {

        Intent intent = new Intent(this, Login_Activity.class);
        intent.putExtra("sign_up",isSignIn);
        startActivity(intent);
        finish();

    }
    public void Logout() {
        final ProgressDialog dialog = Common.createProgressDialog(this, getString(R.string.wait));
        dialog.show();
        Api.getService()
                .Logout(userModel.getUser_id())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()) {
                            /*new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            manager.cancelAll();
                                        }
                                    },1);
                            userSingleTone.clear(ClientHomeActivity.this);*/
                            preferences.create_update_userdata(HomeActivity.this, null);
                            preferences.create_update_session(HomeActivity.this, Tags.session_logout);
                            Intent intent = new Intent(HomeActivity.this, Login_Activity.class);
                            startActivity(intent);
                            finish();
                            if (cuurent_language.equals("ar")) {
                                //  overridePendingTransition(R.anim.from_left,R.anim.to_right);


                            } else {
                                //  overridePendingTransition(R.anim.from_right,R.anim.to_left);


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }

    private void addVisit(final String timeNow) {

        Api.getService()
                .updateVisit("android", timeNow)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful()) {
                            preferences.saveVisitTime(HomeActivity.this, timeNow);
                            // Log.e("msg",response.body().toString());

                        } else {
                            try {
                                Log.e("error_code", response.code() + response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        try {
                            Log.e("Error", t.getMessage());
                        } catch (Exception e) {
                        }
                    }
                });

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1){
            if(!data.getStringExtra("result").equals("-1")){
            DisplayFragmentAdversimentDetials(data.getStringExtra("result"));
        }}
        else {
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onActivityResult(requestCode, resultCode, data);}
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (Fragment fragment : fragmentList) {
            fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }



}
