package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.search_activity.SearchUsersActivity;
import com.creativeshare.hand_break.models.UserSearchDataModel;
import com.creativeshare.hand_break.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.MyHolder> {
    private List<UserSearchDataModel.UserSearchModel> userSearchModelList;
    private Context context;
    private SearchUsersActivity activity;

    public UserSearchAdapter(Context context,List<UserSearchDataModel.UserSearchModel> userSearchModelList) {
        this.userSearchModelList = userSearchModelList;
        this.context = context;
        activity = (SearchUsersActivity) context;

    }

    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.user_search_row, viewGroup, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder,  int position) {
        UserSearchDataModel.UserSearchModel userSearchModel = userSearchModelList.get(position);
        holder.BindData(userSearchModel);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSearchDataModel.UserSearchModel userSearchModel = userSearchModelList.get(holder.getAdapterPosition());
                activity.setItemData(userSearchModel);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userSearchModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_name;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            image = itemView.findViewById(R.id.image);

        }

        private void BindData(UserSearchDataModel.UserSearchModel userSearchModel)
        {
            tv_name.setText(userSearchModel.getUser_name());
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+userSearchModel.getUser_image())).placeholder(R.drawable.logoa).fit().into(image);
        }


    }
}