package com.creativeshare.hand_break.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.models.Catogry_Model;

import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Spinner_Sub_Sub_catogry_Adapter extends BaseAdapter {
    private List<Catogry_Model.Categories.sub.Sub> subs;
    private LayoutInflater inflater;
    private String current_language;

    public Spinner_Sub_Sub_catogry_Adapter(Context context, List<Catogry_Model.Categories.sub.Sub> subs) {
        this.subs = subs;
        inflater = LayoutInflater.from(context);
        Paper.init(context);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
    }

    @Override
    public int getCount() {
        return subs.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.spinner_row, parent, false);

        }
        TextView tv_name = convertView.findViewById(R.id.tv_name);

        Catogry_Model.Categories.sub.Sub sub = subs.get(position);

            tv_name.setText(sub.getModel_title());

        return convertView;
    }
}
