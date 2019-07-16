package com.creativeshare.hand_break.activities_fragments.home_activity.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Home;

import java.util.Locale;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private int fragment_count=0;
    private Fragment_Home fragment_home;
    private String cuurent_language;

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
    }
    public void DisplayFragmentnotifications() {
    }
    public void DisplayFragmentSearch() {
    }



    public void DisplayFragmentMore() {
    }


}
