package com.creativeshare.hand_break.services;



import com.creativeshare.hand_break.models.UserModel;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;


public interface Service {
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> SignIn(@Field("email") String phone,
                           @Field("password") String password
    );


}
