package com.creativeshare.hand_break.services;



import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.models.AppDataModel;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.UserModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;


public interface Service {
    @FormUrlEncoded
    @POST("Api/login")
    Call<UserModel> SignIn(@Field("user_name") String user_name,
                           @Field("user_pass") String user_pass
    );
    @FormUrlEncoded
    @POST("Api/signup")
    Call<UserModel> Signup(@Field("user_email") String user_email,
                           @Field("user_name") String user_name,
                           @Field("user_phone") String user_phone,
                           @Field("user_phone_code") String user_phone_code,
                           @Field("soft_type") String soft_type,
                           @Field("user_pass") String user_pass
    );
    @FormUrlEncoded
    @POST("Api/logout")
    Call<ResponseBody> Logout(@Field("user_id") String user_id

    );
    @GET("Api/termsConditions") Call<AppDataModel>getterms(
            @Header("device-lang") String device_lang

    );
    @GET("Api/aboutApp") Call<AppDataModel>getabout(
            @Header("device-lang") String device_lang

    );
    @GET("Api/advertisingTerms") Call<AppDataModel>getadsterms(
            @Header("device-lang") String device_lang

    );
    @FormUrlEncoded
    @POST("Api/visit")
    Call<ResponseBody> updateVisit(@Field("type") String type, @Field("day_date") String day_date);

    @GET("App/showCategories")
    Call<Catogry_Model> getcateogries(
            @Header("device-lang") String device_lang
    );
    @GET("Api/cyties")
    Call<List<CityModel>> getCities(
            @Header("device-lang") String device_lang


    );
    @GET("App/showAdvertsing") Call<Catogry_Model>
    getadversment(
            @Query("page") int page,
            @Query("user_id") String user_id,
            @Query("main_category_fk") String main_category_fk,
            @Query("sub_category_fk") String sub_category_fk

            );

}
