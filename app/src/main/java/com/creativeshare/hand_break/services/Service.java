package com.creativeshare.hand_break.services;


import com.creativeshare.hand_break.models.Adversiment_Comment_Model;
import com.creativeshare.hand_break.models.Adversiting_Model;
import com.creativeshare.hand_break.models.CityModel;
import com.creativeshare.hand_break.models.AppDataModel;
import com.creativeshare.hand_break.models.Catogry_Model;
import com.creativeshare.hand_break.models.Follower_Model;
import com.creativeshare.hand_break.models.Insuarce_Data_Model;
import com.creativeshare.hand_break.models.Insuarce_Model;
import com.creativeshare.hand_break.models.MessageDataModel;
import com.creativeshare.hand_break.models.MessageModel;
import com.creativeshare.hand_break.models.Notification_Model;
import com.creativeshare.hand_break.models.RoomIdModel;
import com.creativeshare.hand_break.models.UserModel;
import com.creativeshare.hand_break.models.UserRoomModelData;
import com.creativeshare.hand_break.models.UserSearchDataModel;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
    @POST("Api/updateProfile")
    Call<UserModel> updateprofile(
            @Field("user_id") String user_id,
            @Field("user_email") String user_email,
            @Field("user_name") String user_name,
            @Field("user_phone") String user_phone,
            @Field("user_phone_code") String user_phone_code,
            @Field("user_address") String user_address,
            @Field("user_pass") String user_pass,
            @Field("user_city") String user_city


    );

    @Multipart
    @POST("Api/updateProfile")
    Call<UserModel> udateimage(@Part("user_id") RequestBody user_id,

                               @Part MultipartBody.Part advertisement_images

    );

    @GET("App/showProfile")
    Call<UserModel> Showotherprofile(@Query("user_id") String user_id,
                                     @Query("other_user_id_fk") String other_user_id_fk
    );

    @FormUrlEncoded
    @POST("App/followUser")
    Call<ResponseBody> followuser(@Field("user_id") String user_id,
                                  @Field("other_user_id_fk") String other_user_id_fk
    );

    @FormUrlEncoded
    @POST("Api/logout")
    Call<ResponseBody> Logout(@Field("user_id") String user_id

    );

    @GET("Api/termsConditions")
    Call<AppDataModel> getterms(
            @Header("device-lang") String device_lang

    );

    @GET("Api/aboutApp")
    Call<AppDataModel> getabout(
            @Header("device-lang") String device_lang

    );

    @GET("Api/advertisingTerms")
    Call<AppDataModel> getadsterms(
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

    @GET("App/showAdvertsing")
    Call<Catogry_Model>
    getadversment(
            @Query("page") int page,
            @Query("user_id") String user_id,
            @Query("main_category_fk") String main_category_fk,
            @Query("sub_category_fk") String sub_category_fk,
            @Query("city_id") String city_id,
            @Query("google_lat") String google_lat,
            @Query("google_long") String google_long
    );

    @GET("App/searchAdvertsing")
    Call<Catogry_Model>
    searchadversment(
            @Query("page") int page,
            @Query("user_id") String user_id,
            @Query("search_name") String search_name

    );

    @GET("App/searchAdvertsing")
    Call<Catogry_Model>
    searchadversment2(
            @Query("page") int page,
            @Query("user_id") String user_id,
            @Query("main_category_fk") String main_category_fk,
            @Query("sub_category_fk") String sub_category_fk,
            @Query("model_id_fk") String model_id_fk

    );

    @GET("App/myAdvertsing")
    Call<Catogry_Model>
    getmyadversment(
            @Query("page") int page,
            @Query("user_id") String user_id


    );

    @GET("App/getOneAdvertsing")
    Call<Adversiting_Model>
    getadversmentdetials(

            @Query("id_advertisement") String id_advertisement,
            @Query("user_id") String user_id


    );
    @GET("App/getOneAdvertsing")
    Call<Adversiting_Model>
    getadversmentdetials(

            @Query("id_advertisement") String id_advertisement


    );
    @Multipart
    @POST("App/addAdvertsing")
    Call<Catogry_Model.Advertsing> addads(@Part("advertisement_user") RequestBody advertisement_user,
                                          @Part("main_category_fk") RequestBody main_category_fk,
                                          @Part("sub_category_fk") RequestBody sub_category_fk,
                                          @Part("model_id_fk") RequestBody model_id_fk,
                                          @Part("advertisement_title") RequestBody advertisement_title,
                                          @Part("advertisement_content") RequestBody advertisement_content,
                                          @Part("advertisement_price") RequestBody advertisement_price,
                                          @Part("city_id") RequestBody city_id,
                                          @Part("phone") RequestBody phone,
                                          @Part("google_lat") RequestBody google_lat,
                                          @Part("google_long") RequestBody google_long,
                                          @Part List<MultipartBody.Part> advertisement_images,
                                          @Part("piece_number") RequestBody piece_number,
                                          @Part("plate_number") RequestBody plate_number,
                                          @Part("advertisement_type") RequestBody advertisement_type

    );

    @Multipart
    @POST("App/updateAdvertsing")
    Call<Adversiting_Model> updateads
            (@Part("advertisement_user") RequestBody advertisement_user,
             @Part("main_category_fk") RequestBody main_category_fk,
             @Part("sub_category_fk") RequestBody sub_category_fk,
             @Part("model_id_fk") RequestBody model_id_fk,
             @Part("advertisement_title") RequestBody advertisement_title,
             @Part("advertisement_content") RequestBody advertisement_content,
             @Part("advertisement_price") RequestBody advertisement_price,
             @Part("city_id") RequestBody city_id,
             @Part("phone") RequestBody phone,
             @Part("id_advertisement") RequestBody id_advertisement,
             @Part("google_lat") RequestBody google_lat,
             @Part("google_long") RequestBody google_long,
             @Part List<MultipartBody.Part> advertisement_images,
             @Part("piece_number") RequestBody piece_number,
             @Part("plate_number") RequestBody plate_number,
             @Part("advertisement_type") RequestBody advertisement_type

            );

    @FormUrlEncoded
    @POST("App/deleteImage")
    Call<Adversiting_Model> deleteimageads(
            @Field("id_advertisement") String id_advertisement,
            @Field("image_id") String image_id


    );

    @FormUrlEncoded
    @POST("App/followAdvertsing")
    Call<ResponseBody> followadversiment(
            @Field("id_advertisement") String id_advertisement,
            @Field("user_id") String user_id


    );

    @FormUrlEncoded
    @POST("App/ratingUser")
    Call<ResponseBody> makerating(
            @Field("user_id") String user_id,
            @Field("rating_user") String rating_user,
            @Field("rating_value") String rating_value


    );

    @FormUrlEncoded
    @POST("App/commentAdvertising")
    Call<Adversiment_Comment_Model> addcomment(
            @Field("id_advertisement") String id_advertisement,
            @Field("user_id") String user_id,
            @Field("comment_text") String comment_text


    );

    @GET("App/AdvertisingComments")
    Call<Adversiment_Comment_Model> getcomments(@Query("id_advertisement") String id_advertisement,
                                                @Query("page") int page
    );

    @GET("App/followers")
    Call<Follower_Model> getfollowers(@Query("user_id") String user_id,
                                      @Query("type") String type
    );

    @GET("Chating/rooms")
    Call<UserRoomModelData> getRooms(@Query("user_id") String user_id,
                                     @Query("page") int page
    );

    @FormUrlEncoded
    @POST("Chating/searchUser")
    Call<UserSearchDataModel> searchUsers(@Field("search_name") String search_name,
                                          @Field("user_id") String user_id
    );

    @FormUrlEncoded
    @POST("Chating/getChatRoom")
    Call<RoomIdModel> getRoomId(@Field("from_user_id") String from_user_id,
                                @Field("to_user_id") String to_user_id
    );

    @FormUrlEncoded
    @POST("Chating/Typing")
    Call<ResponseBody> typing(@Field("room_id_fk") String room_id_fk,
                              @Field("from_user") String from_user_id,
                              @Field("to_user") String to_user_id,
                              @Field("typing_value") int typing_value

    );

    @GET("Chating/chat")
    Call<MessageDataModel> getChatMessages(@Query("user_id") String user_id,
                                           @Query("room_id") String room_id,
                                           @Query("page") int page);

    @FormUrlEncoded
    @POST("Chating/chating")
    Call<MessageModel> sendMessage(@Field("room_id_fk") String room_id_fk,
                                   @Field("from_user") String from_user,
                                   @Field("to_user") String to_user,
                                   @Field("message") String message

    );


    @FormUrlEncoded
    @POST("Chating/deleteChatRoom")
    Call<ResponseBody> deleteChat(@Field("room_id") String room_id,
                                  @Field("user_id") String user_id

    );

    @FormUrlEncoded
    @POST("Api/updateFirebaseToken")
    Call<ResponseBody> updateToken(@Field("user_id") String user_id,
                                   @Field("phone_token") String user_token_id

    );

    @GET("Note/me")
    Call<Notification_Model>
    getnotification(

            @Query("user_id") String user_id,
            @Query("page") int page


    );

    @Multipart
    @POST("Insurance/add")
    Call<Insuarce_Model> Addinsurance
            (@Part("car_owner_id") RequestBody car_owner_id,
             @Part("car_owner_phone") RequestBody car_owner_phone,
             @Part("car_owner_name") RequestBody car_owner_name,
             @Part("insurance_type") RequestBody insurance_type,
             @Part("personal_id_num") RequestBody personal_id_num,
             @Part("personal_id_date") RequestBody personal_id_date,
             @Part("car_model") RequestBody car_model,
             @Part("car_type") RequestBody car_type,

             @Part MultipartBody.Part form_image,
             @Part MultipartBody.Part car_image


            );

    @FormUrlEncoded
    @POST("Insurance/sendOffer")
    Call<ResponseBody> sendinsuranceoffer
            (@Field("user_id") String user_id,
             @Field("client_id") String client_id,
             @Field("id_notification") String id_notification,
             @Field("request_id") String request_id,
             @Field("offer_value") String offer_value

            );

    @FormUrlEncoded
    @POST("Insurance/refuseSendOffer")
    Call<ResponseBody> refuesinsuranceoffer
            (@Field("user_id") String user_id,
             @Field("client_id") String client_id,
             @Field("id_notification") String id_notification,
             @Field("request_id") String request_id


            );

    @FormUrlEncoded
    @POST("Insurance/replyOffer")
    Call<ResponseBody> acceptrefuesinsuranceoffer
            (@Field("user_id") String user_id,
             @Field("client_id") String client_id,
             @Field("id_notification") String id_notification,
             @Field("request_id") String request_id,
             @Field("status") String status


            );

    @GET("Insurance/show")
    Call<Insuarce_Data_Model>
    showinsurancerequsts(

            @Query("user_id") String user_id


    );
}
