package com.creativeshare.hand_break.activities_fragments.ads_activity.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.creativeshare.hand_break.R;
import com.creativeshare.hand_break.activities_fragments.ads_activity.activity.AdsActivity;
import com.creativeshare.hand_break.models.Adversiment_Model;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.preferences.Preferences;
import com.creativeshare.hand_break.remote.Api;
import com.creativeshare.hand_break.share.Common;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.paperdb.Paper;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Ads_Adder_Info extends Fragment implements GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener, OnMapReadyCallback {
    private AdsActivity adsActivity;
    private String cuurent_language;
    private Preferences preferences;
    private UserModel userModel;
   // private CountryCodePicker ccp_phone;
    private EditText edt_title, edt_phone, edt_price, edt_desc;
    private Button bt_add;
    private Adversiment_Model adversiment_model;
    private String adversiment_id;
    private final String gps_perm = Manifest.permission.ACCESS_FINE_LOCATION;
    private final int gps_req = 22;
   // private final int loc_req = 1225;
    private double lat = 0.0, lng = 0.0;
    private float zoom = 15.6f;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;
    private Location location;
    private boolean stop = false;

    private Marker marker;
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ads_adder_info, container, false);
        updateUI();
        initView(view);
        CheckPermission();

        return view;
    }

    private void updateUI() {

        SupportMapFragment fragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment_map);
        fragment.getMapAsync(this);

    }


    private void AddMarker(double lat, double lng) {

        this.lat = lat;
        this.lng = lng;

        if (marker == null) {
            IconGenerator iconGenerator = new IconGenerator(adsActivity);
            iconGenerator.setBackground(null);
            View view = LayoutInflater.from(adsActivity).inflate(R.layout.search_map_icon, null);
            iconGenerator.setContentView(view);
            marker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromBitmap(iconGenerator.makeIcon())).anchor(iconGenerator.getAnchorU(), iconGenerator.getAnchorV()).draggable(true));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));

        } else {
            marker.setPosition(new LatLng(lat, lng));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), zoom));


        }


    }

    private void initView(View view) {
        adversiment_id = Adversiment_Model.getId();
        adsActivity = (AdsActivity) getActivity();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(adsActivity);
        Paper.init(adsActivity);
        cuurent_language = Paper.book().read("lang", Locale.getDefault().getLanguage());
        edt_title = view.findViewById(R.id.edt_title);
        edt_phone = view.findViewById(R.id.edt_phone);
        edt_price = view.findViewById(R.id.edt_price);
        edt_desc = view.findViewById(R.id.edt_desc);
        //ccp_phone = view.findViewById(R.id.ccp);
        bt_add = view.findViewById(R.id.bt_add);
        //ccp_phone.registerCarrierNumberEditText(edt_phone);

        if (!adversiment_id.equals("-1")) {
            bt_add.setText(getResources().getString(R.string.edit));
        }
        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = edt_title.getText().toString();
                String phone = edt_phone.getText().toString();
                String price = edt_price.getText().toString();
                String desc = edt_desc.getText().toString();
                if (title.isEmpty() || phone.isEmpty() || desc.isEmpty()) {
                    if (title.isEmpty()) {
                        edt_title.setError(getResources().getString(R.string.field_req));
                    }
                    if (phone.isEmpty()) {
                        edt_phone.setError("");
                    }
                    if (desc.isEmpty()) {
                        edt_desc.setError(getResources().getString(R.string.field_req));

                    }
                } else {
                    if (adversiment_id.equals("-1")) {
                       // Log.e("mdg", adversiment_id);
                        createads(title,  phone, price, desc,lat,lng);
                    } else {
                       // Log.e("msg", lat+" "+lng);

                        updateeads(title,  phone, price, desc, adversiment_id,lat,lng);

                    }
                }
            }
        });
    }

    public void setdata() {
        Adversiting_Model adversiting_model = Adversiment_Model.getAdversiting_model();
        if (adversiting_model != null) {
            edt_title.setText(adversiting_model.getAdvertisement_title());
         //   ccp_phone.setFullNumber(adversiting_model.getPhone());

            edt_phone.setText(adversiting_model.getPhone());
            edt_desc.setText(adversiting_model.getAdvertisement_content());
            edt_price.setText(adversiting_model.getAdvertisement_price());
lat=Double.parseDouble(adversiting_model.getGoogle_lat());
        lng=Double.parseDouble(adversiting_model.getGoogle_long());
           // Log.e("mfg", lat+" "+lng+" "+adversiting_model.getGoogle_lat());

            AddMarker(lat,lng);
        }
    }

    private void updateeads(String title, String phone, String price, String desc, String adversiment_id, double lat, double lng) {

        final Dialog dialog = Common.createProgressDialog(adsActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser_id());
        RequestBody cat_part = Common.getRequestBodyText(adversiment_model.getCat_id());
        //RequestBody sub_part = Common.getRequestBodyText(adversiment_model.getSub_id());
       // RequestBody model_part = Common.getRequestBodyText(adversiment_model.getModel_id());
        RequestBody city_part = Common.getRequestBodyText(adversiment_model.getCity_id());
        //RequestBody piece_part = Common.getRequestBodyText(adversiment_model.getPiece());
      //  RequestBody plate_part = Common.getRequestBodyText(adversiment_model.getPalte());
        RequestBody type_part = Common.getRequestBodyText(adversiment_model.getType());
        RequestBody title_part = Common.getRequestBodyText(title);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody price_part = Common.getRequestBodyText(price);
        RequestBody desc_part = Common.getRequestBodyText(desc);
        RequestBody lat_part = Common.getRequestBodyText(lat+"");
        RequestBody lng_part = Common.getRequestBodyText(lng+"");
        //Log.e("nnn",lat+"  "+lng);

        RequestBody adversiment_part = Common.getRequestBodyText(adversiment_id);

        List<MultipartBody.Part> partImageList = getMultipartBodyList(adversiment_model.getUris(), "advertisement_images[]");

            Api.getService().updateads(user_part, cat_part,  title_part, desc_part, price_part, city_part, phone_part, adversiment_part,lat_part,lng_part, partImageList,type_part).enqueue(new Callback<Adversiting_Model>() {
                @Override
                public void onResponse(Call<Adversiting_Model> call, Response<Adversiting_Model> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        // Common.CreateSignAlertDialog(adsActivity,getResources().getString(R.string.suc));

                        adsActivity.finish(response.body().getId_advertisement());
                    } else {
                        try {

                        Common.CreateSignAlertDialog(adsActivity, getResources().getString(R.string.failed));
                        Log.e("Error", response.code() + "" + response.errorBody() + response.raw() + response.body() + response.headers());
                        }catch (Exception e){


                    }
                    }
                }

                @Override
                public void onFailure(Call<Adversiting_Model> call, Throwable t) {
                    dialog.dismiss();
                    try {
                        Toast.makeText(adsActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                        Log.e("Error", t.getMessage());
                    }
                    catch (Exception e){

                    }
                }
            });

    }

    private void createads(String title, String phone, String price, String desc, double lat, double lng) {

        final Dialog dialog = Common.createProgressDialog(adsActivity, getString(R.string.wait));
        dialog.setCancelable(false);
        dialog.show();
        RequestBody user_part = Common.getRequestBodyText(userModel.getUser_id());
        RequestBody cat_part = Common.getRequestBodyText(adversiment_model.getCat_id());
      //  RequestBody sub_part = Common.getRequestBodyText(adversiment_model.getSub_id());
        //RequestBody model_part = Common.getRequestBodyText(adversiment_model.getModel_id());
        RequestBody city_part = Common.getRequestBodyText(adversiment_model.getCity_id());
        //RequestBody piece_part = Common.getRequestBodyText(adversiment_model.getPiece());
       // RequestBody plate_part = Common.getRequestBodyText(adversiment_model.getPalte());
        RequestBody type_part = Common.getRequestBodyText(adversiment_model.getType());
        RequestBody title_part = Common.getRequestBodyText(title);
        RequestBody phone_part = Common.getRequestBodyText(phone);
        RequestBody price_part = Common.getRequestBodyText(price);
        RequestBody desc_part = Common.getRequestBodyText(desc);
        RequestBody lat_part = Common.getRequestBodyText(lat+"");
        RequestBody lng_part = Common.getRequestBodyText(lng+"");
        List<MultipartBody.Part> partImageList = getMultipartBodyList(adversiment_model.getUris(), "advertisement_images[]");
        Api.getService().addads(user_part, cat_part, title_part, desc_part, price_part, city_part, phone_part,lat_part,lng_part,partImageList,type_part).enqueue(new Callback<Catogry_Model.Advertsing>() {
            @Override
            public void onResponse(Call<Catogry_Model.Advertsing> call, Response<Catogry_Model.Advertsing> response) {
                dialog.dismiss();
                if (response.isSuccessful()) {
                    //   Common.CreateSignAlertDialog(adsActivity, getResources().getString(R.string.suc));
                    adsActivity.finish(response.body().getId_advertisement());
                } else {
                    try {
                        Log.e("Error", response.code() + "" + response.errorBody() + response.raw() + response.body() + response.headers());

                        Common.CreateSignAlertDialog(adsActivity, getResources().getString(R.string.failed));

                    }
                    catch (Exception e){

                    }

                }
            }

            @Override
            public void onFailure(Call<Catogry_Model.Advertsing> call, Throwable t) {
                dialog.dismiss();
                try {
                    Toast.makeText(adsActivity, getString(R.string.something), Toast.LENGTH_SHORT).show();
                    Log.e("Error", t.getMessage());
                }
                catch (Exception e){

                }
            }
        });

    }

    public static Fragment_Ads_Adder_Info newInstance() {
        return new Fragment_Ads_Adder_Info();
    }


    public void setmodel(Adversiment_Model adversiment_model) {
        this.adversiment_model = adversiment_model;

    }

    private List<MultipartBody.Part> getMultipartBodyList(List<Uri> uriList, String image_cv) {
        List<MultipartBody.Part> partList = new ArrayList<>();
        for (Uri uri : uriList) {
            MultipartBody.Part part = Common.getMultiPart(adsActivity, uri, image_cv);
            partList.add(part);
        }
        return partList;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1255) {
            if (resultCode == Activity.RESULT_OK) {
                startLocationUpdate();
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == gps_req && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            initGoogleApiClient();
        }
    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(adsActivity, gps_perm) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(adsActivity, new String[]{gps_perm}, gps_req);
        } else {

            initGoogleApiClient();
        }
    }

    private void initGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(adsActivity)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void intLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setFastestInterval(1000 * 60 * 2);
        locationRequest.setInterval(1000 * 60 * 2);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult result) {

                Status status = result.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        startLocationUpdate();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(adsActivity, 1255);
                        } catch (Exception e) {
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Log.e("not available", "not available");
                        break;
                }
            }
        });

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        intLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        this.location = location;

        if (Adversiment_Model.getId().equals("-1")) {
            lng = location.getLongitude();
            lat = location.getLatitude();
            AddMarker(lat, lng);
        }
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }


        if (locationCallback != null) {
            LocationServices.getFusedLocationProviderClient(adsActivity).removeLocationUpdates(locationCallback);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdate() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                onLocationChanged(locationResult.getLastLocation());
            }
        };
        LocationServices.getFusedLocationProviderClient(adsActivity)
                .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap != null) {
            mMap = googleMap;
            mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(adsActivity, R.raw.maps));
            mMap.setTrafficEnabled(false);
            mMap.setBuildingsEnabled(false);
            mMap.setIndoorEnabled(true);

            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    lat = latLng.latitude;
                    lng = latLng.longitude;
                  //  Log.e("nnn",lat+"  "+lng);
                    AddMarker(lat, lng);
                }
            });

        }
    }
}