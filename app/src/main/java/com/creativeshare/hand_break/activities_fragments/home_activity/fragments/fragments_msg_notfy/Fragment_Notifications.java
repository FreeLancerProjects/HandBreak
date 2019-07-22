package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_msg_notfy;

import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Notification_Adapter;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.Notification_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Notifications extends Fragment {
    private HomeActivity homeActivity;
    private String cuurent_language;
    private RecyclerView rec_notify;
    private List<Notification_Model.Data>  notification_list;
    private UserModel userModel;
    private Notification_Adapter notification_adapter;
    private GridLayoutManager manager;
    private int current_page=1;
    private int total_page;
    private boolean isLoading=false;
    private TextView tv_no_notify;
    private ProgressBar progressBar;
    private Preferences preferences;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        initView(view);
        getnotification();
        return view;
    }

    private void getnotification() {
        rec_notify.setVisibility(View.GONE);
        tv_no_notify.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        Api.getService().getnotification(userModel.getUser_id(),1).enqueue(new Callback<Notification_Model>() {
            @Override
            public void onResponse(Call<Notification_Model> call, Response<Notification_Model> response) {
                progressBar.setVisibility(View.GONE);
                if(response.isSuccessful()){

                    if(response.body()!=null&&response.body().getData().size()>0){
                        rec_notify.setVisibility(View.VISIBLE);
                        notification_list.clear();
                        notification_list.addAll(response.body().getData());
                        notification_adapter.notifyDataSetChanged();
                        total_page=response.body().getMeta().getLast_page();
                    }
                    else
                    {
                        tv_no_notify.setVisibility(View.VISIBLE);
                    }
                }

            }

            @Override
            public void onFailure(Call<Notification_Model> call, Throwable t) {
progressBar.setVisibility(View.GONE);
                Toast.makeText(homeActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                Log.e("Error",t.getMessage());
            }
        });
    }

    private void initView(View view) {
        notification_list=new ArrayList<>();
        homeActivity = (HomeActivity) getActivity();
        preferences=Preferences.getInstance();
        userModel=preferences.getUserData(homeActivity);
        Paper.init(homeActivity);
        progressBar=view.findViewById(R.id.progBar);
        progressBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(homeActivity, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);

        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
rec_notify=view.findViewById(R.id.recView_notify);
tv_no_notify=view.findViewById(R.id.tv_no_notfications);
notification_adapter=new Notification_Adapter(notification_list,homeActivity);
        rec_notify.setDrawingCacheEnabled(true);
        rec_notify.setItemViewCacheSize(25);
        rec_notify.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        manager = new GridLayoutManager(homeActivity, 1);
        rec_notify.setLayoutManager(manager);
        rec_notify.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int total_item = manager.getItemCount();
                    int last_item_pos = manager.findLastCompletelyVisibleItemPosition();
                    Log.e("msg", total_item + "  " + last_item_pos);
                    Log.e("msg", current_page+"");

                    if (last_item_pos >= (total_item - 5) && !isLoading&&total_page>current_page) {
                        isLoading = true;
                        notification_list.add(null);
                        notification_adapter.notifyItemInserted(notification_list.size() - 1);
                        int page = current_page + 1;
                        //cuurent_language+=1;
                        Log.e("msg", page+"");

                        loadMore(page);
                    }
                }
            }

        });
        rec_notify.setAdapter(notification_adapter);
         }

    private void loadMore(int page) {
        Api.getService().getnotification(userModel.getUser_id(),page).enqueue(new Callback<Notification_Model>() {
            @Override
            public void onResponse(Call<Notification_Model> call, Response<Notification_Model> response) {
                isLoading = false;
                notification_list.remove(notification_list.size()-1);
                if(response.isSuccessful()){
                    if(response.body()!=null&&response.body().getData().size()>0){
                   current_page=response.body().getMeta().getCurrent_page();
                   notification_list.addAll(response.body().getData());
                   notification_adapter.notifyDataSetChanged();
                }
            }}

            @Override
            public void onFailure(Call<Notification_Model> call, Throwable t) {

            }
        });
    }

    public static Fragment_Notifications newInstance() {
        return new Fragment_Notifications();
    }

}
