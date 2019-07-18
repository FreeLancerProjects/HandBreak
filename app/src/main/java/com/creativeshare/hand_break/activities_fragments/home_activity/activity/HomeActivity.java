package com.creativeshare.hand_break.activities_fragments.home_activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_About;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Home;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Main;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Profile;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_msg_notfy.Fragment_Message_Notifications;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_More;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Search;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Terms_Conditions;
import com.creativeshare.hand_break.activities_fragments.sign_in_sign_up_activity.activity.Login_Activity;
import com.creativeshare.hand_break.language.Language_Helper;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.creativeshare.hand_break.tags.Tags;

import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private int fragment_count=0;
    private Fragment_Home fragment_home;
    private Fragment_Main fragment_main;
    private Fragment_Message_Notifications fragment_message_notifications;
    private Fragment_Search fragment_search;
    private Fragment_More fragment_more;
    private Fragment_Terms_Conditions fragmentTerms_conditions;
private Fragment_About fragment_about;
private Fragment_Profile fragment_profile;
    private String cuurent_language;
    private Preferences preferences;
    private UserModel userModel;
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language_Helper.updateResources(newBase, Preferences.getInstance().getLanguage(newBase)));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initView();

        if (savedInstanceState == null) {
            DisplayFragmentHome();
            DisplayFragmentMain();
        }

    }
    private void initView() {
        Paper.init(this);
        preferences= Preferences.getInstance();
        userModel=preferences.getUserData(this);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        fragmentManager = this.getSupportFragmentManager();
    }
    public void DisplayFragmentHome()
    {
        fragment_count+=1;
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
        if(fragment_main==null){
            fragment_main=Fragment_Main.newInstance();
        }
        if(fragment_message_notifications!=null&&fragment_message_notifications.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_message_notifications).commit();
        }
        if(fragment_search!=null&&fragment_search.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_search).commit();
        }
        if(fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if(fragment_main.isAdded()){
            fragmentManager.beginTransaction().show(fragment_main).commit();
        }
        else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_main, "fragment_main").addToBackStack("fragment_main").commit();

        }
        if(fragment_home!=null&&fragment_home.isAdded()){
            fragment_home.updateBottomNavigationPosition(0);
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
    public void DisplayFragmentProfile() {
        fragment_count += 1;
        fragment_profile = Fragment_Profile.newInstance();


        if (fragment_profile.isAdded()) {
            fragmentManager.beginTransaction().show(fragment_profile).commit();
        } else {
            fragmentManager.beginTransaction().add(R.id.fragment_app_container, fragment_profile, "fragment_profile").addToBackStack("fragment_profile").commit();

        }
    }
    public void DisplayFragmentnotifications() {
        if(fragment_message_notifications==null){
            fragment_message_notifications=Fragment_Message_Notifications.newInstance();
        }
        if(fragment_main!=null&&fragment_main.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if(fragment_search!=null&&fragment_search.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_search).commit();
        }
        if(fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if(fragment_message_notifications.isAdded()){
            fragmentManager.beginTransaction().show(fragment_message_notifications).commit();
        }
        else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_message_notifications, "fragment_message_notifications").addToBackStack("fragment_message_notifications").commit();

        }
        if(fragment_home!=null&&fragment_home.isAdded()){
            fragment_home.updateBottomNavigationPosition(1);
        }
    }
    public void DisplayFragmentSearch() {
        if(fragment_search==null){
            fragment_search=Fragment_Search.newInstance();
        }
        if(fragment_main!=null&&fragment_main.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if(fragment_message_notifications!=null&&fragment_message_notifications.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_message_notifications).commit();
        }
        if(fragment_more!=null&&fragment_more.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_more).commit();
        }
        if(fragment_search.isAdded()){
            fragmentManager.beginTransaction().show(fragment_search).commit();
        }
        else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_search, "fragment_search").addToBackStack("fragment_search").commit();

        }
        if(fragment_home!=null&&fragment_home.isAdded()){
            fragment_home.updateBottomNavigationPosition(2);
        }
    }



    public void DisplayFragmentMore() {
        if(fragment_more==null){
            fragment_more=Fragment_More.newInstance();
        }
        if(fragment_main!=null&&fragment_main.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_main).commit();
        }
        if(fragment_search!=null&&fragment_search.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_search).commit();
        }
        if(fragment_message_notifications!=null&&fragment_message_notifications.isAdded()){
            fragmentManager.beginTransaction().hide(fragment_message_notifications).commit();
        }
        if(fragment_more.isAdded()){
            fragmentManager.beginTransaction().show(fragment_more).commit();
        }
        else {
            fragmentManager.beginTransaction().add(R.id.fragment_home_container, fragment_more, "fragment_more").addToBackStack("fragment_more").commit();

        }
        if(fragment_home!=null&&fragment_home.isAdded()){
            fragment_home.updateBottomNavigationPosition(3);
        }
    }


    public void getoAds() {
        Intent intent=new Intent(HomeActivity.this, AdsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
Back();    }

    public void Back() {
        if (fragment_count > 1) {
            fragment_count -= 1;
            super.onBackPressed();
        } else {

            if (fragment_home != null && fragment_home.isVisible()) {
                if (fragment_main != null && fragment_main.isVisible()) {
                    if (userModel == null) {
                        //NavigateToSignInActivity();
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
    public void Logout()
    {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.show();
        Api.getService()
                .Logout(userModel.getUser_id())
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        dialog.dismiss();
                        if (response.isSuccessful())
                        {
                            /*new Handler()
                                    .postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                            manager.cancelAll();
                                        }
                                    },1);
                            userSingleTone.clear(ClientHomeActivity.this);*/
preferences.create_update_userdata(HomeActivity.this,null);
preferences.create_update_session(HomeActivity.this, Tags.session_logout);
                            Intent intent = new Intent(HomeActivity.this, Login_Activity.class);
                            startActivity(intent);
                            finish();
                            if (cuurent_language.equals("ar"))
                            {
                              //  overridePendingTransition(R.anim.from_left,R.anim.to_right);



                            }else
                            {
                              //  overridePendingTransition(R.anim.from_right,R.anim.to_left);


                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
    }


}
