package com.lol.crashmonitor.network;

public class NetworkManager {

    public static ApiServices getConstantsApi() {
        return RetrofitClient.getClient("add base url")
                .create(ApiServices.class);
    }
}