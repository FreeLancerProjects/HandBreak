package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.Adversiment_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.share.Common;
import com.zcw.togglebutton.ToggleButton;

import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_More extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private ImageView im_log, arrow1, arrow2, arrow3, arrow4, arrow5, arrow6, arrow7, arrow8, arrow9, arrow10, arrow11;
    private LinearLayout ll_logout, ll_terms, ll_about, ll_profile,ll_adversiment,ll_language,ll_addcar,ll_follow;
    private FrameLayout fr_arabic,fr_english;
    private TextView tv_ar,tv_en;
    private Preferences preferences;
    private UserModel userModel;
    private String [] language_array;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        language_array = new String[]{"English","العربية"};

        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(homeActivity);
        fr_arabic=view.findViewById(R.id.f1);
        fr_english=view.findViewById(R.id.f2);
        tv_ar=view.findViewById(R.id.tv_ar);
        tv_en=view.findViewById(R.id.tv_en);
        im_log = view.findViewById(R.id.im_log);
        ll_addcar=view.findViewById(R.id.ll_addcar);
        ll_terms = view.findViewById(R.id.ll_terms);
        ll_about = view.findViewById(R.id.ll_about);
        ll_profile = view.findViewById(R.id.ll_profile);
        ll_follow=view.findViewById(R.id.ll_follow);
        ll_logout = view.findViewById(R.id.ll_logout);
        ll_adversiment=view.findViewById(R.id.ll_advertisement);
       // ll_language=view.findViewById(R.id.ll_lang);
        arrow1 = view.findViewById(R.id.arrow1);
       arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
       // arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        arrow6 = view.findViewById(R.id.arrow6);
        arrow7 = view.findViewById(R.id.arrow7);
       // arrow8 = view.findViewById(R.id.arrow8);
        arrow9 = view.findViewById(R.id.arrow9);
        if(userModel!=null){
            if(!userModel.getUser_type().equals("2")){
                ll_follow.setVisibility(View.GONE);
            }
        }
        //arrow10 = view.findViewById(R.id.arrow10);
       // arrow11 = view.findViewById(R.id.arrow11);
        if (cuurent_language.equals("en")) {
            im_log.setRotation(180.0f);
            fr_arabic.setBackground(getResources().getDrawable(R.drawable.lang_shape2));
            fr_english.setBackground(getResources().getDrawable(R.drawable.lang_shape1));
            tv_ar.setTextColor(getResources().getColor(R.color.delete));
            tv_en.setTextColor(getResources().getColor(R.color.white));

        } else {
            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
           // arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
            arrow6.setRotation(180.0f);
            arrow7.setRotation(180.0f);
          //  arrow8.setRotation(180.0f);
            arrow9.setRotation(180.0f);
            //arrow10.setRotation(180.0f);
         //   arrow11.setRotation(180.0f);
            fr_arabic.setBackground(getResources().getDrawable(R.drawable.lang_shape1));
            fr_english.setBackground(getResources().getDrawable(R.drawable.lang_shape2));
            tv_en.setTextColor(getResources().getColor(R.color.delete));
            tv_ar.setTextColor(getResources().getColor(R.color.white));

        }
        fr_arabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cuurent_language.equals("en")){
                    homeActivity.RefreshActivity("ar");
                }
            }
        });
        fr_english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cuurent_language.equals("ar")){
                    homeActivity.RefreshActivity("en");
                }
            }
        });
        ll_addcar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentAddCar();
            }
        });
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
                    Adversiment_Model.setId(null);
homeActivity.DisplayFragmentProfile();
                }
            }
        });
      /*  ll_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateLanguageDialog();
            }
        });*/
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
        ll_follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userModel!=null){
                    homeActivity.DisplayFragmentfollowers();
                }
            }
        });
    }

    public static Fragment_More newInstance() {
        return new Fragment_More();
    }
    /*private void CreateLanguageDialog()
    {
        final AlertDialog dialog = new AlertDialog.Builder(homeActivity)
                .setCancelable(true)
                .create();

        View view  = LayoutInflater.from(homeActivity).inflate(R.layout.dialog_language,null);
        Button btn_select = view.findViewById(R.id.btn_select);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);

        final NumberPicker numberPicker = view.findViewById(R.id.numberPicker);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(language_array.length-1);
        numberPicker.setDisplayedValues(language_array);
        numberPicker.setWrapSelectorWheel(false);
        if (cuurent_language.equals("ar"))
        {
            numberPicker.setValue(2);

        }else if (cuurent_language.equals("en"))
        {
            numberPicker.setValue(0);

        }
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                int pos = numberPicker.getValue();
                if (pos == 0)
                {
                    homeActivity.RefreshActivity("en");
                }
                else
                {
                    homeActivity.RefreshActivity("ar");

                }

            }
        });




        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.getWindow().getAttributes().windowAnimations = R.style.dialog_congratulation_animation;
        dialog.setView(view);
        dialog.show();
    }*/
}
