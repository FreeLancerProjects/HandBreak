package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_insurance_cars;


import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Spinner_Adapter;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.share.Common;
import com.mukesh.countrypicker.CountryPicker;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import io.paperdb.Paper;

public class Fragment_insurance_car extends Fragment {

    private EditText phone,name,id_num,car_type;
    private ImageView image_phone_code;
    private LinearLayout ll_date;
    private TextView tv_code,tv_date;
    private Spinner spinner_model;
    private long date=0;
    private Spinner_Adapter model_adapter;
    private DatePickerDialog datePickerDialog;
    private HomeActivity activity;
    private String current_language;
    private String code = "";
    private String model_id="";

    public static Fragment_insurance_car newInstance() {
        return new Fragment_insurance_car();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insurance_car,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        image_phone_code = view.findViewById(R.id.image_phone_code);

        if (current_language.equals("ar")||current_language.equals("ur"))
        {
            image_phone_code.setRotation(180.0f);
        }

        tv_code = view.findViewById(R.id.tv_code);
        phone = view.findViewById(R.id.edt_phone);
        name = view.findViewById(R.id.edt_name);
        id_num=view.findViewById(R.id.edt_residency_number);
        tv_date = view.findViewById(R.id.tv_date);
        car_type = view.findViewById(R.id.edt_car_type);
        ll_date = view.findViewById(R.id.ll_date);

        spinner_model = view.findViewById(R.id.spinner_model);

        spinner_model.setAdapter(model_adapter);

        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position ==0)
                {
                    model_id ="";
                }else
                {
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        image_phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.getShowsDialog();
            }
        });

        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(activity.getFragmentManager(),"");
            }
        });
        createDatePickerDialog();

    }

    public boolean isDataOk()
    {
        String m_phone = phone.getText().toString().trim();
        String m_name = name.getText().toString();
        String m_car_typee = car_type.getText().toString();
        String m_id_num = id_num.getText().toString();

        if (!TextUtils.isEmpty(m_phone)&&
                !TextUtils.isEmpty(code)&&
                !TextUtils.isEmpty(m_name)&&
                !TextUtils.isEmpty(m_car_typee)&&
                !TextUtils.isEmpty(m_id_num)&&
                !TextUtils.isEmpty(model_id)&&
                date!=0
        )
        {
            tv_code.setError(null);
            phone.setError(null);
            name.setError(null);
            car_type.setError(null);
            id_num.setError(null);
            tv_date.setError(null);
            Common.CloseKeyBoard(activity,phone);
            return true;
        }else
        {
            if (TextUtils.isEmpty(code))
            {
                tv_code.setError(getString(R.string.field_req));
            }else
            {
                tv_code.setError(null);

            }
            if (TextUtils.isEmpty(m_phone))
            {
                phone.setError(getString(R.string.field_req));
            }else
            {
                phone.setError(null);

            }

            if (TextUtils.isEmpty(m_name))
            {
                name.setError(getString(R.string.field_req));
            }else
            {
                name.setError(null);

            }

            if (TextUtils.isEmpty(m_car_typee))
            {
                car_type.setError(getString(R.string.field_req));
            }else
            {
                car_type.setError(null);

            }

            if (TextUtils.isEmpty(m_id_num))
            {
                id_num.setError(getString(R.string.field_req));
            }else
                id_num.setError(null);

            }
            if (date==0)
            {
                tv_date.setError(getString(R.string.field_req));
            }

            if (TextUtils.isEmpty(model_id))
            {
                Toast.makeText(activity, getString(R.string.field_req), Toast.LENGTH_SHORT).show();
            }

            return false;
        }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance((DatePickerDialog.OnDateSetListener) this,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity,R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity,R.color.colorPrimary));
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(current_language));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

}





