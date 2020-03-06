package com.ats.edetailingapp.interfaces;

import com.ats.edetailingapp.model.LoginModel;
import com.ats.edetailingapp.model.SelectionDataModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface InterfaceApi {

    @GET("validate_login/format/json")
    Call<LoginModel> doLogin(@Query("username") String username, @Query("password") String password, @Query("imei") String imei);

    @GET("get_tag_by_id/format/json")
    Call<SelectionDataModel> getAllData(@Query("username") String username, @Query("password") String password, @Query("imei") String imei, @Query("u_id") int u_id);

}
