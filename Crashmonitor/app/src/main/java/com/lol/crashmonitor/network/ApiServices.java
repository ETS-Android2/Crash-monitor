package com.lol.crashmonitor.network;

import com.lol.crashmonitor.model.constant.ConstantsResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiServices {

    // For get the user data
    @GET("constant")
    Call<ArrayList<ConstantsResponse>> getConstants(@Query("version") String version);
}