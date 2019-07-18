package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_msg_notfy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_Message_Notifications extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private TabLayout tab;
    private ViewPager pager;
    private ViewPagerAdapter adapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    private int image[];
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_notification, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tab = view.findViewById(R.id.tab);
        pager = view.findViewById(R.id.pager);
        tab.setupWithViewPager(pager);
        pager.setOffscreenPageLimit(3);
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();
image=new int[]{R.drawable.notifications,R.drawable.msg};
        fragmentList.add(Fragment_Notifications.newInstance());
        fragmentList.add(Fragment_Message.newInstance());

        titleList.add(getString(R.string.notifications));
        titleList.add(getString(R.string.message));
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.AddFragments(fragmentList);

        pager.setAdapter(adapter);
        createTabIcons();

        tab.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View v=tab.getCustomView();
                TextView tv_tab=v.findViewById(R.id.tv_tab);
                ImageView im_tab=v.findViewById(R.id.im_tab);

                tv_tab.setTextColor(getResources().getColor(R.color.delete));
                im_tab.setColorFilter(getResources().getColor(R.color.delete));

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View v=tab.getCustomView();
                TextView tv_tab=v.findViewById(R.id.tv_tab);
              ImageView im_tab=v.findViewById(R.id.im_tab);

                tv_tab.setTextColor(getResources().getColor(R.color.colorPrimary));
                im_tab.setColorFilter(getResources().getColor(R.color.colorPrimary));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public static Fragment_Message_Notifications newInstance() {
        return new Fragment_Message_Notifications();
    }
    private void createTabIcons() {
        LinearLayout []tabs=new LinearLayout[titleList.size()];
for (int i=0;i<titleList.size();i++){
    tabs[i]= (LinearLayout) LayoutInflater.from(homeActivity).inflate(R.layout.tab_item, null);
    TextView tv_tab=tabs[i].findViewById(R.id.tv_tab);
    ImageView im_tab=tabs[i].findViewById(R.id.im_tab);
    if(i==0){
        tv_tab.setTextColor(getResources().getColor(R.color.delete));
        im_tab.setColorFilter(getResources().getColor(R.color.delete));
    }
    else{
        tv_tab.setTextColor(getResources().getColor(R.color.colorPrimary));
        im_tab.setColorFilter(getResources().getColor(R.color.colorPrimary));
    }
tv_tab.setText(titleList.get(i));
im_tab.setImageResource(image[i]);
    tab.getTabAt(i).setCustomView(tabs[i]);
}

    }
}
