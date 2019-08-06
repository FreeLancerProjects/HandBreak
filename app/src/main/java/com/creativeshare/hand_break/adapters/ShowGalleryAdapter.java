package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads_Detials;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.tags.Tags;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowGalleryAdapter extends RecyclerView.Adapter<ShowGalleryAdapter.MyHolder> {

    private List<Adversiting_Model.Advertisement_images> advertisement_images;
    private Context context;
    private Fragment_Ads_Detials fragment_ads_detials;
    public ShowGalleryAdapter(List<Adversiting_Model.Advertisement_images> advertisement_images, Context context, Fragment_Ads_Detials fragment_ads_detials) {
        this.advertisement_images = advertisement_images;
        this.context = context;
        this.fragment_ads_detials = fragment_ads_detials;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_gv_item, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Adversiting_Model.Advertisement_images advertisement_image = advertisement_images.get(position);

        Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL+advertisement_image.getImage_name())).fit().into(holder.ivGallery);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment_ads_detials.Deleteimageapi(advertisement_images.get(holder.getLayoutPosition()).getImage_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return advertisement_images.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder {

        private RoundedImageView ivGallery;
        private ImageView delete;


        public MyHolder(View itemView) {
            super(itemView);
            ivGallery = itemView.findViewById(R.id.ivGallery);
            delete = itemView.findViewById(R.id.delete_img1);
        }


    }


}