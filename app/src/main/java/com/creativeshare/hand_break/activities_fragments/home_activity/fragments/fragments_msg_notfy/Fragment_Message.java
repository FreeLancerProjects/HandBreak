package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_msg_notfy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.chat_activity.ChatActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.activities_fragments.search_activity.SearchUsersActivity;
import com.creativeshare.hand_break.adapters.Rooms_Adapter;
import com.creativeshare.hand_break.models.ChatUserModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.models.UserRoomModelData;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Message extends Fragment {
    private HomeActivity activity;
    private TextView tv_no_msg;
    private ProgressBar progBar;
    private RecyclerView recView;
    private LinearLayoutManager manager;
    private LinearLayout ll_search;
    private String cuurent_language;
    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private Rooms_Adapter adapter;
    private Preferences preferences;
    private UserModel userModel;
    private int current_page=1;
    private boolean isLoading = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        userRoomModelList = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(activity);
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        tv_no_msg = view.findViewById(R.id.tv_no_msg);
        ll_search = view.findViewById(R.id.ll_search);
        progBar = view.findViewById(R.id.progBar);
        progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        recView = view.findViewById(R.id.recView);
        manager = new LinearLayoutManager(activity);
        recView.setLayoutManager(manager);
        adapter = new Rooms_Adapter(activity,userRoomModelList,this);
        recView.setAdapter(adapter);
        recView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0)
                {
                    int total_item = adapter.getItemCount();
                    int lastVisibleItemPos = manager.findLastCompletelyVisibleItemPosition();

                    if (total_item>5&&(lastVisibleItemPos==total_item-2)&&!isLoading)
                    {
                        isLoading = true;
                        int next_page = current_page+1;
                        loadMore(next_page);
                    }
                }

            }
        });


        ll_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, SearchUsersActivity.class);
                startActivityForResult(intent,1123);
            }
        });

        if (userModel!=null)
        {
            getRooms();

        }else
            {
                tv_no_msg.setVisibility(View.VISIBLE);
            }

    }

    public void getRooms() {
        Api.getService()
                .getRooms(userModel.getUser_id(),1)
                .enqueue(new Callback<UserRoomModelData>() {
                    @Override
                    public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                        progBar.setVisibility(View.GONE);
                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            if (response.body().getData().size()>0)
                            {
                                userRoomModelList.clear();
                                userRoomModelList.addAll(response.body().getData());
                                tv_no_msg.setVisibility(View.GONE);
                                adapter.notifyDataSetChanged();

                            }else
                                {
                                    tv_no_msg.setVisibility(View.VISIBLE);
                                }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                        try {
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    private void loadMore(final int page_index) {
        Api.getService()
                .getRooms(userModel.getUser_id(),page_index)
                .enqueue(new Callback<UserRoomModelData>() {
                    @Override
                    public void onResponse(Call<UserRoomModelData> call, Response<UserRoomModelData> response) {
                        isLoading = false;
                        userRoomModelList.remove(userRoomModelList.size()-1);

                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null)
                        {
                            if (response.body().getData().size()>0)
                            {
                                current_page = response.body().getMeta().getCurrent_page();
                                userRoomModelList.addAll(response.body().getData());
                                adapter.notifyDataSetChanged();

                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<UserRoomModelData> call, Throwable t) {
                        try {
                            isLoading=false;
                            userRoomModelList.remove(userRoomModelList.size()-1);
                            adapter.notifyDataSetChanged();
                            progBar.setVisibility(View.GONE);
                            Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });
    }

    public static Fragment_Message newInstance() {
        return new Fragment_Message();
    }

    public void setItemData(UserRoomModelData.UserRoomModel userRoomModel) {
        ChatUserModel chatUserModel = new ChatUserModel(userRoomModel.getSecond_user_name(),userRoomModel.getSecond_user_image(),userRoomModel.getSecond_user(),userRoomModel.getRoom_id(),userRoomModel.getDate_registration());
        Intent intent = new Intent(activity, ChatActivity.class);
        intent.putExtra("data",chatUserModel);
        intent.putExtra("from",true);
        startActivityForResult(intent,1124);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1123&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            getRooms();
        }else if (requestCode==1124&&resultCode== Activity.RESULT_OK&&data!=null)
        {
            getRooms();
        }
    }
}
