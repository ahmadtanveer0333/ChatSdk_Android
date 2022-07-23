package com.example.chatsdk_android;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiMethods {
//    @GET("api/users?page=2")
//    Call<ModelClass> allData();
//
//    @POST("GetAllActiveVisitors")
//    Call<AllActiveUsers> allActiveVisitors();
//
//    @POST("GetAllConnectedVisitors")
//    Call<AllActiveUsers> allConnectedVisitors();

    @FormUrlEncoded
    @POST("Token")
    Call<TokenResponse> getToken(@Field("grant_type") String grantType,
                                 @Field("username") String userName,
                                 @Field("password") String password);

}