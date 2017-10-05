package com.prostage.l_pha.dental_user.server;

import com.google.gson.JsonObject;
import com.prostage.l_pha.dental_user.model.server_model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by USER on 15-May-17.
 */

public interface DentalAPI {
    @GET("/getToken")
    Call<ResponseModel> getToken();

    @GET("/usersInfo/{id}")
    Call<ResponseModel> getUserInfoById(@Path("id") int id);

    @GET("/adminsInfo/{id}")
    Call<ResponseModel> getAdminInfoById(@Path("id") int id);

    //change password
    @POST("/usersInfo/changepass/{id}")
    Call<ResponseModel> setPassword(@Path("id") int id, @Body JsonObject data);

    //change name
    @POST("/usersInfo/{id}")
    Call<ResponseModel> setName(@Path("id") int id, @Body JsonObject data);
}
