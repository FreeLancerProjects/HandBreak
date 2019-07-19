package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Home;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Main;
import com.creativeshare.hand_break.models.Catogry_Model;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class CatogriesAdapter extends RecyclerView.Adapter<CatogriesAdapter.Eyas_Holder> {
    List<Catogry_Model.Categories> list;
    Context context;
    private HomeActivity homeActivity;
    private int select;
    Fragment_Home fragment_home;

    public CatogriesAdapter(List<Catogry_Model.Categories> list, Context context, Fragment_Home fragment_home) {
        this.list = list;
        this.context = context;
        homeActivity = (HomeActivity) context;
        this.fragment_home = fragment_home;

    }

    @Override
    public Eyas_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.catogry_row, viewGroup, false);
        Eyas_Holder eas = new Eyas_Holder(v);
        return eas;
    }

    @Override
    public void onBindViewHolder(@NonNull final Eyas_Holder viewHolder, final int i) {
        Catogry_Model.Categories model = list.get(i);
        viewHolder.tv_name.setText(model.getMain_category_title());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select = i;
                notifyDataSetChanged();
                homeActivity.DisplayFragmentMain();
                fragment_home.setsub(list.get(i).getsub(),list.get(i).getMain_category_fk());
            }

        });
        if (select == i) {
            viewHolder.tv_name.setTextColor(homeActivity.getResources().getColor(R.color.colorPrimary));
            viewHolder.view.setBackgroundColor(homeActivity.getResources().getColor(R.color.colorPrimary));

        } else {
            viewHolder.tv_name.setTextColor(homeActivity.getResources().getColor(R.color.black));
            viewHolder.view.setBackgroundColor(homeActivity.getResources().getColor(R.color.white));
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Eyas_Holder extends RecyclerView.ViewHolder {
        TextView tv_name;

        View view;

        public Eyas_Holder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_title);

            view = itemView.findViewById(R.id.view);
        }


    }
}