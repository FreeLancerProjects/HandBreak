package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class Fragment_Profile extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private CircleImageView imageprofile;
    private TextView tv_name,tv_loaction,tv_address,tv_commericial,tv_phone,tv_email;
    private ImageView  arrow1, arrow2, arrow3, arrow4, arrow5,im_edit;
    private Preferences preferences;
    private UserModel userModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        homeActivity = (HomeActivity) getActivity();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        imageprofile=view.findViewById(R.id.image);
        tv_name=view.findViewById(R.id.tv_name);
        tv_loaction=view.findViewById(R.id.tv_location);
        tv_address=view.findViewById(R.id.tv_address);
        tv_commericial=view.findViewById(R.id.tv_commercial);
        tv_phone=view.findViewById(R.id.tv_phone);
        tv_email=view.findViewById(R.id.tv_email);
        arrow1 = view.findViewById(R.id.arrow1);
        arrow2 = view.findViewById(R.id.arrow2);
        arrow3 = view.findViewById(R.id.arrow3);
        arrow4 = view.findViewById(R.id.arrow4);
        arrow5 = view.findViewById(R.id.arrow5);
        im_edit=view.findViewById(R.id.im_edit);
        if(cuurent_language.equals("en"))
        {
            arrow1.setRotation(180.0f);
            arrow2.setRotation(180.0f);
            arrow3.setRotation(180.0f);
            arrow4.setRotation(180.0f);
            arrow5.setRotation(180.0f);
        }
        updateprofile();
im_edit.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        homeActivity.DisplayFragmentEditProfile();
    }
});
    }

    private void updateprofile() {
        if(userModel!=null){
            if(userModel.getUser_image()!=null&&!userModel.getUser_image().equals("0")){
                Picasso.with(homeActivity).load(Tags.IMAGE_URL+userModel.getUser_image()).fit().into(imageprofile);
            }
            if(userModel.getUser_name()!=null){
                tv_name.setText(userModel.getUser_name());
            }
            if(userModel.getUser_city()!=null){
                tv_loaction.setText(userModel.getUser_city());
            }
            if(userModel.getUser_address()!=null){
                tv_address.setText(userModel.getUser_address());
            }
            if(userModel.getCommercial_register()!=null){
                tv_commericial.setText(userModel.getCommercial_register());
            }
            if(userModel.getUser_phone()!=null){
                tv_phone.setText(userModel.getUser_phone());
            }
            if(userModel.getUser_email()!=null){
                tv_email.setText(userModel.getUser_email());
            }
        }
    }

    public static Fragment_Profile newInstance() {
        return new Fragment_Profile();
    }

}
