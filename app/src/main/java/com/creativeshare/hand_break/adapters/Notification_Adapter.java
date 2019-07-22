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
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.Fragment_Home;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.Notification_Model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.Eyas_Holder> {
    List<Notification_Model.Data> list;
    Context context;
    private HomeActivity homeActivity;
 ;

    public Notification_Adapter(List<Notification_Model.Data> list, Context context) {
        this.list = list;
        this.context = context;
        homeActivity = (HomeActivity) context;

    }

    @Override
    public Eyas_Holder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_row, viewGroup, false);
        Eyas_Holder eas = new Eyas_Holder(v);
        return eas;
    }

    @Override
    public void onBindViewHolder(@NonNull final Eyas_Holder viewHolder, final int i) {
        Notification_Model.Data model = list.get(i);
        viewHolder.tv_title.setText(model.getContent_notification());
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale.ENGLISH);
        String date = dateFormat.format(new Date(Long.parseLong(model.getDate_notification()) * 1000));
       viewHolder.tv_time.setText(date);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(!list.get(viewHolder.getLayoutPosition()).getAction_type().equals("0")){
    homeActivity.DisplayFragmentAdversimentDetials(list.get(viewHolder.getLayoutPosition()).getId_advertisement());
}
            }

        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Eyas_Holder extends RecyclerView.ViewHolder {
        private TextView tv_title,tv_time;


        public Eyas_Holder(@NonNull View itemView) {
            super(itemView);
            tv_title = itemView.findViewById(R.id.tv_title);

            tv_time = itemView.findViewById(R.id.tv_time);
        }


    }
}