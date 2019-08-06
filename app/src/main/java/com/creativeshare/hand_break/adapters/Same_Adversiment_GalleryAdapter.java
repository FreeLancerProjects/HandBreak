package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads_Detials;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Same_Adversiment_GalleryAdapter extends RecyclerView.Adapter<Same_Adversiment_GalleryAdapter.MyHolder> {

    private List<Adversiting_Model.Same_advertisements> same_advertisements;
    private Context context;
private HomeActivity homeActivity;
    public Same_Adversiment_GalleryAdapter(List<Adversiting_Model.Same_advertisements> same_advertisements, Context context) {
        this.same_advertisements = same_advertisements;
        this.context = context;
        homeActivity=(HomeActivity)context;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.same_adversiment_row, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Adversiting_Model.Same_advertisements same_advertisement = same_advertisements.get(position);

       // Log.e("ssssss",uri.toString());
        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+same_advertisement.getMain_image())).fit().into(holder.ivGallery);
holder.progBar.setVisibility(View.GONE);
       // getimage(same_advertisement.getId_advertisement(),holder.ivGallery,holder.progBar);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                homeActivity.DisplayFragmentAdversimentDetials(same_advertisements.get(holder.getLayoutPosition()).getId_advertisement());
            }
        });

    }



    @Override
    public int getItemCount() {
        return same_advertisements.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        private CircleImageView ivGallery;
        private ProgressBar progBar;


        public MyHolder(View itemView) {
            super(itemView);
            ivGallery = itemView.findViewById(R.id.ivGallery);
            progBar = itemView.findViewById(R.id.progBar);
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);


        }


    }


}