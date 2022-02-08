package com.lol.crashmonitor.network;

public class NetworkManager {

    public static ApiServices getConstantsApi() {
        return RetrofitClient.getClient("https://apig.unscripted.news/unscripted/")
                .create(ApiServices.class);
    }
}