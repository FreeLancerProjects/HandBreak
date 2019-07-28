package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Main;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.Follower_Model;
import com.creativeshare.hand_break.tags.Tags;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Followers_Adapter extends RecyclerView.Adapter<Followers_Adapter.Eyas_Holder> {
    List<Follower_Model.Data> list;
    Context context;


    public Followers_Adapter(List<Follower_Model.Data> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public Eyas_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.followers_row, viewGroup, false);
        Eyas_Holder eas = new Eyas_Holder(v);
        return eas;
    }

    @Override
    public void onBindViewHolder(@NonNull final Eyas_Holder viewHolder, final int i) {
        Follower_Model.Data model = list.get(i);
        viewHolder.tv_name.setText(model.getUser_name());
        Picasso.with(context).load(Tags.IMAGE_URL+model.getUser_image()).fit().into(viewHolder.im_follow);



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Eyas_Holder extends RecyclerView.ViewHolder {
        TextView tv_name;

       CircleImageView im_follow;

        public Eyas_Holder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);

            im_follow = itemView.findViewById(R.id.im_follow);
        }


    }
}