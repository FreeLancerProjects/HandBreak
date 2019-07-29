package com.creativeshare.hand_break.activities_fragments.home_activity.fragments.fragments_insurance_cars;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.home_activity.activity.HomeActivity;
import com.creativeshare.hand_break.adapters.Spinner_Adapter;
import com.creativeshare.hand_break.share.Common;
import com.hbb20.CountryCodePicker;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;

public class Fragment_insurance_car extends Fragment implements DatePickerDialog.OnDateSetListener {

    private final String READ_PERM = Manifest.permission.READ_EXTERNAL_STORAGE;
    private final String write_permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private final String camera_permission = Manifest.permission.CAMERA;
    private final int IMG_REQ1 = 1, IMG_REQ2 = 2;
    private Uri imgUri1 = null, imgUri2 = null;
    private EditText phone, name, id_num, car_type;
    private CountryCodePicker countryCodePicker;
private EditText btn_send;
    // private ImageView image_phone_code;
    private LinearLayout ll_date;
    private TextView tv_code, tv_date;
    private Spinner spinner_model;
    private long date = 0;
    // private Spinner_Adapter model_adapter;
    private DatePickerDialog datePickerDialog;
    private HomeActivity activity;
    private String current_language;
    private String code = "";
    private String model_id = "";
    private List<String> model_ids;
    private ArrayAdapter<String> arrayAdapter;
    private FrameLayout fl1,fl2;
    private ImageView icon1,icon2;
    private RoundedImageView image1,image2;
    public static Fragment_insurance_car newInstance() {
        return new Fragment_insurance_car();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_insurance_car, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        model_ids = new ArrayList<>();
        model_ids.addAll(Arrays.asList(getResources().getStringArray(R.array.models)));
        activity = (HomeActivity) getActivity();
        Paper.init(activity);
        current_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        //image_phone_code = view.findViewById(R.id.image_phone_code);
        arrayAdapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, model_ids);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        if (current_language.equals("ar")) {
            //   image_phone_code.setRotation(180.0f);
        }

        tv_code = view.findViewById(R.id.tv_code);
        phone = view.findViewById(R.id.edt_phone);
        name = view.findViewById(R.id.edt_name);
        id_num = view.findViewById(R.id.edt_residency_number);
        tv_date = view.findViewById(R.id.tv_date);
        car_type = view.findViewById(R.id.edt_car_type);
        ll_date = view.findViewById(R.id.ll_date);
        countryCodePicker = view.findViewById(R.id.ccp);
        countryCodePicker.registerCarrierNumberEditText(phone);
        spinner_model = view.findViewById(R.id.spinner_model);
        fl1 = view.findViewById(R.id.fl1);
        fl2 = view.findViewById(R.id.fl2);
        icon1 = view.findViewById(R.id.icon_upload_car);
        icon2 = view.findViewById(R.id.icon_form);
        image1=view.findViewById(R.id.image_car);
        image2=view.findViewById(R.id.image_form);
        btn_send=view.findViewById(R.id.btn_send);
        spinner_model.setAdapter(arrayAdapter);
        tv_code.setText(countryCodePicker.getSelectedCountryCode());

        countryCodePicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                tv_code.setText(countryCodePicker.getSelectedCountryCode());
            }
        });
        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    model_id = "";
                } else {
                    model_id = spinner_model.getSelectedItem().toString();
                    Log.e("llll", model_id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckReadPermission(IMG_REQ1);
            }
        });

        fl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckReadPermission(IMG_REQ2);
            }
        });

    /*    image_phone_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.getShowsDialog();
            }
        });*/

        ll_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show(activity.getFragmentManager(), "");
            }
        });
        createDatePickerDialog();
btn_send.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        isDataOk();
    }
});
    }

    public boolean isDataOk() {
        String m_phone = phone.getText().toString().trim();
        String m_name = name.getText().toString();
        String m_car_typee = car_type.getText().toString();
        String m_id_num = id_num.getText().toString();

        if (!TextUtils.isEmpty(m_phone) &&
                !TextUtils.isEmpty(code) &&
                !TextUtils.isEmpty(m_name) &&
                !TextUtils.isEmpty(m_car_typee) &&
                !TextUtils.isEmpty(m_id_num) &&
                !TextUtils.isEmpty(model_id) &&
                date != 0
        ) {
            tv_code.setError(null);
            phone.setError(null);
            name.setError(null);
            car_type.setError(null);
            id_num.setError(null);
            tv_date.setError(null);
            Common.CloseKeyBoard(activity, phone);
            return true;
        } else {
            if (TextUtils.isEmpty(code)) {
                tv_code.setError(getString(R.string.field_req));
            } else {
                tv_code.setError(null);

            }
            if (TextUtils.isEmpty(m_phone)) {
                phone.setError(getString(R.string.field_req));
            } else {
                phone.setError(null);

            }

            if (TextUtils.isEmpty(m_name)) {
                name.setError(getString(R.string.field_req));
            } else {
                name.setError(null);

            }

            if (TextUtils.isEmpty(m_car_typee)) {
                car_type.setError(getString(R.string.field_req));
            } else {
                car_type.setError(null);

            }

            if (TextUtils.isEmpty(m_id_num)) {
                id_num.setError(getString(R.string.field_req));
            } else
                id_num.setError(null);

        }
        if (date == 0) {
            tv_date.setError(getString(R.string.field_req));
        }

        if (TextUtils.isEmpty(model_id)) {
            Toast.makeText(activity, getString(R.string.field_req), Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    private void createDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.dismissOnPause(true);
        datePickerDialog.setAccentColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setCancelColor(ActivityCompat.getColor(activity, R.color.gray4));
        datePickerDialog.setOkColor(ActivityCompat.getColor(activity, R.color.colorPrimary));
        datePickerDialog.setOkText(getString(R.string.select));
        datePickerDialog.setCancelText(getString(R.string.cancel));
        datePickerDialog.setLocale(new Locale(current_language));
        datePickerDialog.setVersion(DatePickerDialog.Version.VERSION_2);
        datePickerDialog.setMinDate(calendar);


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, monthOfYear);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


        // order_time_calender.set(Calendar.YEAR,year);
        //order_time_calender.set(Calendar.MONTH,monthOfYear);
        //order_time_calender.set(Calendar.DAY_OF_MONTH,dayOfMonth);


        tv_date.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
        date = calendar.getTimeInMillis();

    }


    private void CheckReadPermission(int img_req)
    {
        if (ActivityCompat.checkSelfPermission(activity, READ_PERM) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{READ_PERM}, img_req);
        } else {
            SelectImage(img_req);
        }
    }


    private void SelectImage(int img_req) {

        Intent intent = new Intent();


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            {
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            }else
            {
                intent.setAction(Intent.ACTION_GET_CONTENT);

            }

            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/*");
            startActivityForResult(intent,img_req);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == IMG_REQ1) {


                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(IMG_REQ1);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }


        } else if (requestCode == IMG_REQ2) {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SelectImage(IMG_REQ2);
                } else {
                    Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();
                }

        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMG_REQ1 && resultCode == Activity.RESULT_OK && data != null) {

                imgUri1 = data.getData();
                icon1.setVisibility(View.GONE);
                File file = new File(Common.getImagePath(activity, imgUri1));
                Picasso.with(activity).load(file).fit().into(image1);





        } else if (requestCode == IMG_REQ2 && resultCode == Activity.RESULT_OK && data != null) {


                imgUri2 = data.getData();
                icon2.setVisibility(View.GONE);
                File file = new File(Common.getImagePath(activity, imgUri2));

                Picasso.with(activity).load(file).fit().into(image2);




        }

    }

  /*  private Uri getUriFromBitmap(Bitmap bitmap) {
        String path = "";
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), bitmap, "title", null);
            return Uri.parse(path);

        } catch (SecurityException e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(activity, getString(R.string.perm_image_denied), Toast.LENGTH_SHORT).show();

        }
        return null;
    }*/
}





