package com.creativeshare.hand_break.services;



import com.creativeshare.hand_break.models.UserModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Service {
    @FormUrlEncoded
    @POST("api/login")
    Call<UserModel> SignIn(@Field("email") String phone,
                           @Field("password") String password
    );


}
