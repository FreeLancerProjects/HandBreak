package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.models.AppDataModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;

import java.util.Locale;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 *
 */
public class Fragment_Adviersiment_Terms_Conditions extends Fragment {
    private Preferences preferences;
    private ImageView back;
    private TextView tv_content;
    private AdsActivity adsActivity;
    private String cuurent_language;
private Button bt_ok;
    public static Fragment_Adviersiment_Terms_Conditions newInstance() {

Fragment_Adviersiment_Terms_Conditions fragment_terms_conditions=new Fragment_Adviersiment_Terms_Conditions();
    return fragment_terms_conditions;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terms__conditions, container, false);
        intitview(view);
        // Inflate the layout for this fragment
        return view;
    }

    private void intitview(View view) {
        adsActivity = (AdsActivity) getActivity();
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        preferences=Preferences.getInstance();
        tv_content =view.findViewById(R.id.tv_content);
        back = (ImageView) view.findViewById(R.id.arrow_back);
        bt_ok=view.findViewById(R.id.bt_ok);
        preferences = Preferences.getInstance();

        if (cuurent_language.equals("en")) {

            back.setRotation(180);
        }
        getAppData(cuurent_language);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adsActivity.Back();
            }
        });
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adsActivity.Back();
            }
        });
    }
    private void getAppData(String cuurent_language) {

        Api.getService()
                .getadsterms(cuurent_language)
                .enqueue(new Callback<AppDataModel>() {
                    @Override
                    public void onResponse(Call<AppDataModel> call, Response<AppDataModel> response) {
                      //  smoothprogressbar.setVisibility(View.GONE);

                        if (response.isSuccessful()&&response.body()!=null)
                        {
                            updateTermsContent(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<AppDataModel> call, Throwable t) {
                        try {
                           // smoothprogressbar.setVisibility(View.GONE);
                            Toast.makeText(adsActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                            Log.e("Error",t.getMessage());
                        }catch (Exception e){}
                    }
                });

    }

    private void updateTermsContent(AppDataModel appDataModel) {

            tv_content.setText(appDataModel.getContent());


    }
}
