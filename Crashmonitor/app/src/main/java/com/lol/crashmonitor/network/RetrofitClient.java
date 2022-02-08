package com.lol.crashmonitor.network;

import com.google.gson.GsonBuilder;
import com.lol.vitalmonitor.crashhandler.CustomInterceptor;

import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    static Retrofit getClient(String baseUrl) {
        return new Retrofit.Builder().baseUrl(baseUrl)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory
                        .create(new GsonBuilder().serializeNulls()
                                .excludeFieldsWithModifiers(Modifier.FINAL,
                                        Modifier.TRANSIENT, Modifier.STATIC)
                                .create()))
                .build();
    }

    private static OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        if (BuildConfig.DEBUG) {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
//        } else {
//            interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
//        }

        CustomInterceptor customInterceptor = new CustomInterceptor();
        OkHttpClient httpClient = new OkHttpClient();
        OkHttpClient.Builder builder = httpClient.newBuilder();
        builder.addInterceptor(interceptor);
        builder.addInterceptor(customInterceptor);
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        return builder.build();
    }
}