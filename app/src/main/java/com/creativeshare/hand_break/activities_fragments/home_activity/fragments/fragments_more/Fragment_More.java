package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.share.Common;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private ImageView im_log, arrow1, arrow2, arrow3, arrow4, arrow5, arrow6, arrow7, arrow8, arrow9, arrow10, arrow11;
    private LinearLayout ll_logout, ll_terms, ll_about, ll_profile,ll_adversiment;
    private Preferences preferences;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        im_log = view.findViewById(R.id.im_log);
        ll_terms = view.findViewById(R.id.ll_terms);
        ll_about = view.findViewById(R.id.ll_about);
        ll_profile = view.findViewById(R.id.ll_profile);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_adversiment=view.findViewById(R.id.ll_advertisement);
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        arrow6 = view.findViewById(R.id.arrow6);
        arrow7 = view.findViewById(R.id.arrow7);
        arrow8 = view.findViewById(R.id.arrow8);
        arrow9 = view.findViewById(R.id.arrow9);
        arrow10 = view.findViewById(R.id.arrow10);
        arrow11 = view.findViewById(R.id.arrow11);
        if (cuurent_language.equals("en")) {
            im_log.setRotation(180.0f);
        } else {
            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
            arrow6.setRotation(180.0f);
            arrow7.setRotation(180.0f);
            arrow8.setRotation(180.0f);
            arrow9.setRotation(180.0f);
            arrow10.setRotation(180.0f);
            arrow11.setRotation(180.0f);


        }
        ll_adversiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel==null){
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                }
                else {
                    homeActivity.DisplayFragmentMYAdversiment();
                }
            }
        });
        ll_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentAbout();
            }
        });
        ll_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentTerms_Condition();
            }
        });
        ll_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel==null){
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                }
                else {
homeActivity.DisplayFragmentProfile();
                }
            }
        });
        ll_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userModel == null) {
                    Common.CreateUserNotSignInAlertDialog(homeActivity);
                } else {
                    homeActivity.Logout();
                }
            }
        });
    }

    public static Fragment_More newInstance() {
        return new Fragment_More();
    }

}
