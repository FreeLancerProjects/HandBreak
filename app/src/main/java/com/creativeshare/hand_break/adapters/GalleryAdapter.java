package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;


import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.fragments.Fragment_Ads_Detials;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more.Fragment_Add_Car;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.MyHolder> {

    private List<Uri> advertisement_images;
    private Context context;
    private Fragment_Ads_Detials fragment_ads_detials;
    private Fragment_Add_Car fragment_add_car;
private Fragment fragment;
    public GalleryAdapter(List<Uri> advertisement_images, Context context, Fragment fragment) {
        this.advertisement_images = advertisement_images;
        this.context = context;
this.fragment=fragment;  }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.layout_gv_item, parent, false);
        return new MyHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, int position) {

        Uri uri = advertisement_images.get(position);
       // Log.e("ssssss",uri.toString());
        Picasso.with(context).load(uri).fit().into(holder.ivGallery);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment instanceof  Fragment_Ads_Detials){
                    fragment_ads_detials=(Fragment_Ads_Detials)fragment;
                fragment_ads_detials.Delete(holder.getLayoutPosition());}
                else if(fragment instanceof Fragment_Add_Car){
                    fragment_add_car=(Fragment_Add_Car)fragment;
                    fragment_add_car.Delete(holder.getLayoutPosition());

                }
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