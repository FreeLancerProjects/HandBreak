package com.creativeshare.hand_break.services;



import com.creativeshare.hand_break.models.AppDataModel;
import com.creativeshare.hand_break.models.UserModel;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
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
            @Query("device-lang") String device_lang

    );
    @GET("Api/aboutApp") Call<AppDataModel>getabout(
            @Query("device-lang") String device_lang

    );
    @GET("Api/advertisingTerms") Call<AppDataModel>getadsterms(
            @Query("device-lang") String device_lang

    );
}
