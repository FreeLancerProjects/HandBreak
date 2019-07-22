package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_msg_notfy.Fragment_Message;
import com.creativeshare.hand_break.models.UserRoomModelData;
import com.creativeshare.hand_break.tags.Tags;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Rooms_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_DATA = 1;
    private final int ITEM_LOAD = 2;

    private List<UserRoomModelData.UserRoomModel> userRoomModelList;
    private Context context;
    private Fragment_Message fragment;

    public Rooms_Adapter(Context context, List<UserRoomModelData.UserRoomModel> userRoomModelList, Fragment_Message fragment) {

        this.userRoomModelList = userRoomModelList;
        this.context = context;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_DATA) {
            View view = LayoutInflater.from(context).inflate(R.layout.room_row, parent, false);
            return new MyHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.load_more_row, parent, false);
            return new LoadMoreHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MyHolder) {

            UserRoomModelData.UserRoomModel userRoomModel = userRoomModelList.get(position);
            final MyHolder myHolder = (MyHolder) holder;
            myHolder.BindData(userRoomModel);
            myHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserRoomModelData.UserRoomModel userRoomModel = userRoomModelList.get(myHolder.getAdapterPosition());
                    fragment.setItemData(userRoomModel);
                }
            });
        } else {
            LoadMoreHolder loadMoreHolder = (LoadMoreHolder) holder;
            loadMoreHolder.progBar.setIndeterminate(true);
        }
    }


    @Override
    public int getItemCount() {
        return userRoomModelList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private CircleImageView image;
        private TextView tv_name, tv_date, tv_lastMsg;

        public MyHolder(View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_lastMsg = itemView.findViewById(R.id.tv_lastMsg);


        }

        private void BindData(UserRoomModelData.UserRoomModel userRoomModel) {
            Picasso.with(context).load(Uri.parse(Tags.IMAGE_URL + userRoomModel.getSecond_user_image())).placeholder(R.drawable.logoa).fit().into(image);
            tv_name.setText(userRoomModel.getSecond_user_name());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy\nhh:mm aa", Locale.ENGLISH);
            String date = dateFormat.format(new Date(Long.parseLong(userRoomModel.getMessage_date()) * 1000));
            tv_date.setText(date);
            if (!userRoomModel.getMessage_text().equals("0"))
            {
                tv_lastMsg.setText(userRoomModel.getMessage_text());
            }

        }

    }

    public class LoadMoreHolder extends RecyclerView.ViewHolder {
        private ProgressBar progBar;

        public LoadMoreHolder(View itemView) {
            super(itemView);
            progBar = itemView.findViewById(R.id.progBar);
            progBar.getIndeterminateDrawable().setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    @Override
    public int getItemViewType(int position) {
        UserRoomModelData.UserRoomModel userRoomModel = userRoomModelList.get(position);
        if (userRoomModel == null) {
            return ITEM_LOAD;
        } else {
            return ITEM_DATA;

        }


    }
}
