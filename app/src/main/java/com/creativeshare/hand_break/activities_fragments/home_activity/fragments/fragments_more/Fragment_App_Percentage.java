package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_more;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Bank_Adapter;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.AppDataModel;
import com.creativeshare.hand_break.models.App_Commission;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */
public class Fragment_App_Percentage extends Fragment {
    private Preferences preferences;
    private EditText edt_per;
    private TextView tv_price, tv_commission;
    private double price;
    private ImageView back;
    private HomeActivity activity;
    private String cuurent_language;
    private RecyclerView rec_bank;
    private List<App_Commission.Data> data;
    private Bank_Adapter bank_adapter;

    public static Fragment_App_Percentage newInstance() {

        Fragment_App_Percentage fragment_app_percentage = new Fragment_App_Percentage();
        return fragment_app_percentage;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_precentage, container, false);
        intitview(view);
        getappcommission();
        // Inflate the layout for this fragment
        return view;
    }

    private void getbankaccounts() {


        Api.getService().getappbankaccount().enqueue(new Callback<App_Commission>() {
            @Override
            public void onResponse(Call<App_Commission> call, Response<App_Commission> response) {

                if (response.isSuccessful() && response.body() != null) {
                    updatebankaccout(response.body());
                }
            }

            @Override
            public void onFailure(Call<App_Commission> call, Throwable t) {
                try {
                    // smoothprogressbar.setVisibility(View.GONE);
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    private void updatebankaccout(App_Commission body) {
        data.clear();
        data.addAll(body.getData());
        bank_adapter.notifyDataSetChanged();
    }

    private void getappcommission() {
        getbankaccounts();
        final ProgressDialog dialog = Common.createProgressDialog(activity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        Api.getService().getappcommission().enqueue(new Callback<App_Commission>() {
            @Override
            public void onResponse(Call<App_Commission> call, Response<App_Commission> response) {

                dialog.dismiss();
                if (response.isSuccessful() && response.body() != null) {
                    updateappcommission(response.body());
                }
            }

            @Override
            public void onFailure(Call<App_Commission> call, Throwable t) {
                try {
                    dialog.dismiss();
                    // smoothprogressbar.setVisibility(View.GONE);
                    Toast.makeText(activity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                } catch (Exception e) {
                }
            }
        });
    }

    private void updateappcommission(App_Commission body) {
        price = body.getPer();
        //  data.clear();
        //  data.addAll(body.getData());
        // bank_adapter.notifyDataSetChanged();
    }

    private void intitview(View view) {
        data = new ArrayList<>();
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences = Preferences.getInstance();
        edt_per = view.findViewById(R.id.edt_per);
        tv_price = view.findViewById(R.id.tv_price);
        tv_commission = view.findViewById(R.id.tv_commission);
        back = view.findViewById(R.id.arrow_back);
        rec_bank = view.findViewById(R.id.rec_bank);
        rec_bank.setLayoutManager(new GridLayoutManager(activity, 1));
        bank_adapter = new Bank_Adapter(data, activity);
        rec_bank.setAdapter(bank_adapter);
        preferences = Preferences.getInstance();

        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }
        edt_per.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    tv_price.setText(((Integer.parseInt(charSequence.toString()) * price) / 100) + "");
                    tv_commission.setText(((Integer.parseInt(charSequence.toString()) * price) / 100) + "");

                } catch (Exception e) {
                    tv_price.setText("0");
                    tv_commission.setText("0");

                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        back.setOnClickListener(

                new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.CloseKeyBoard(activity,edt_per);
                activity.Back();
            }
        });
    }


}
