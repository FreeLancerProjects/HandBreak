package com.creativeshare.hand_break.activities_fragments.chat_user_info_activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.language.Language_Helper;
import com.creativeshare.hand_break.models.ChatUserModel;
import com.creativeshare.hand_break.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import io.paperdb.Paper;

public class ChatUserInfoActivity extends AppCompatActivity {
    private ImageView image_back;
    private LinearLayout ll_back, ll_messaging;
    private TextView tv_title, tv_name,tv_member;
    private CircleImageView image;
    private String current_lang;

    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase,Paper.book().read("lang",Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_user_info);
        initView();
        getDataFromIntent();

    }



    private void initView() {
        Paper.init(this);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());

        image_back = findViewById(R.id.image_back);
        if (current_lang.equals("ar"))
        {
            image_back.setRotation(180.0f);
        }

        ll_back = findViewById(R.id.ll_back);
        ll_messaging = findViewById(R.id.ll_messaging);
        tv_title = findViewById(R.id.tv_title);
        tv_name = findViewById(R.id.tv_name);
        image = findViewById(R.id.image);
        tv_member = findViewById(R.id.tv_member);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ll_messaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent!=null)
        {
            ChatUserModel chatUserModel = (ChatUserModel) intent.getSerializableExtra("data");
            updateUI(chatUserModel);
        }
    }

    private void updateUI(ChatUserModel chatUserModel) {
        tv_name.setText(chatUserModel.getName());
        tv_title.setText(chatUserModel.getName());
        Picasso.with(this).load(Uri.parse(Tags.IMAGE_URL+chatUserModel.getImage())).placeholder(R.drawable.logoa).fit().into(image);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa",Locale.ENGLISH);
        String date = dateFormat.format(new Date(Long.parseLong(chatUserModel.getRegistration_date())*1000));
        tv_member.setText(String.format("%s %s",getString(R.string.member_since),date));
    }
}
