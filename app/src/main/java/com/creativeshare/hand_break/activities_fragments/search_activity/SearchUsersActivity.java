package com.creativeshare.hand_break.activities_fragments.search_activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.chat_activity.ChatActivity;
import com.creativeshare.hand_break.adapters.UserSearchAdapter;
import com.creativeshare.hand_break.language.Language_Helper;
import com.creativeshare.hand_break.models.ChatUserModel;
import com.creativeshare.hand_break.models.RoomIdModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.models.UserSearchDataModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUsersActivity extends AppCompatActivity {
    private ImageView arrow, image_search;
    private EditText edt_search;
    private ProgressBar progBar;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private String current_lang;
    private Preferences preferences;
    private UserModel userModel;
    private List<UserSearchDataModel.UserSearchModel> userSearchModelList;
    private UserSearchAdapter adapter;
    private boolean isCreateChat=false;


    @Override
    protected void attachBaseContext(Context newBase) {
        Paper.init(newBase);
        super.attachBaseContext(Language_Helper.updateResources(newBase,Paper.book().read("lang",Locale.getDefault().getLanguage())));

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
        initView();
    }

    private void initView() {
        userSearchModelList = new ArrayList<>();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        Paper.init(this);
        current_lang = Paper.book().read("lang", Locale.getDefault().getLanguage());
        arrow = findViewById(R.id.arrow);
        if (current_lang.equals("ar")) {
            arrow.setRotation(180.0f);
        }
        image_search = findViewById(R.id.image_search);
        edt_search = findViewById(R.id.edt_search);
        progBar = findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = findViewById(R.id.recView);
        manager = new LinearLayoutManager(this);
        recView.setLayoutManager(manager);
        adapter = new UserSearchAdapter(this,userSearchModelList);
        recView.setAdapter(adapter);
        arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Back();
            }
        });

        edt_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String query = edt_search.getText().toString().trim();
                    if (!TextUtils.isEmpty(query)) {
                        search(query);
                    }
                }
                return false;
            }
        });
        image_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = edt_search.getText().toString().trim();
                if (!TextUtils.isEmpty(query)) {
                    search(query);
                }
            }
        });
    }

    private void search(String query) {
        userSearchModelList.clear();
        progBar.setVisibility(View.VISIBLE);

        Api.getService()
                .searchUsers(query,userModel.getUser_id())
                .enqueue(new Callback<UserSearchDataModel>() {
                    @Override
                    public void onResponse(Call<UserSearchDataModel> call, Response<UserSearchDataModel> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            userSearchModelList.addAll(response.body().getData());
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserSearchDataModel> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(SearchUsersActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public void setItemData(UserSearchDataModel.UserSearchModel userSearchModel) {
        if(userSearchModel.getRoom_id().equals("0"))
        {
            getChatRoomId(userSearchModel);
        }else
            {
                ChatUserModel chatUserModel = new ChatUserModel(userSearchModel.getUser_name(),userSearchModel.getUser_image(),userSearchModel.getUser_id(),userSearchModel.getRoom_id(),userSearchModel.getDate_registration());
                Intent intent = new Intent(SearchUsersActivity.this, ChatActivity.class);
                intent.putExtra("data",chatUserModel);
                intent.putExtra("from",true);
                startActivity(intent);
            }
    }

    private void getChatRoomId(final UserSearchDataModel.UserSearchModel userSearchModel) {
        final ProgressDialog dialog = Common.createProgressDialog(this,getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();

        Api.getService()
                .getRoomId(userModel.getUser_id(),userSearchModel.getUser_id())
                .enqueue(new Callback<RoomIdModel>() {
                    @Override
                    public void onResponse(Call<RoomIdModel> call, Response<RoomIdModel> response) {
                        dialog.dismiss();
                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            isCreateChat = true;
                           userSearchModel.setRoom_id(response.body().getRoom_id());

                            ChatUserModel chatUserModel = new ChatUserModel(userSearchModel.getUser_name(),userSearchModel.getUser_image(),userSearchModel.getUser_id(),userSearchModel.getRoom_id(),userSearchModel.getDate_registration());
                            Intent intent = new Intent(SearchUsersActivity.this, ChatActivity.class);
                            intent.putExtra("data",chatUserModel);
                            intent.putExtra("from",true);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onFailure(Call<RoomIdModel> call, Throwable t) {
                        try {
                            dialog.dismiss();
                            Toast.makeText(SearchUsersActivity.this, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Back();
    }

    private void Back() {
        if (isCreateChat)
        {
            Intent intent = getIntent();
            setResult(RESULT_OK,intent);
        }
        finish();

    }
}
