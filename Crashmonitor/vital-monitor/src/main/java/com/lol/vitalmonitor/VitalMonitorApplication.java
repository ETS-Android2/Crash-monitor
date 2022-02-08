package com.lol.vitalmonitor;

import android.app.Application;

import androidx.annotation.NonNull;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lol.vitalmonitor.model.CrashPayload;
import com.lol.vitalmonitor.model.NetworkCallData;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

import timber.log.Timber;

public class VitalMonitorApplication extends Application {

    public static LoadingCache<Long, NetworkCallData> networkCallDataLoadingCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, NetworkCallData>() {
                @Override
                public @NotNull NetworkCallData load(@NotNull Long key) {
                    return null;
                }
            });

    public static LoadingCache<Long, CrashPayload> crashDataLoadingCache = CacheBuilder.newBuilder()
            .maximumSize(10)
            .expireAfterWrite(15, TimeUnit.SECONDS)
            .build(new CacheLoader<Long, CrashPayload>() {
                @Override
                public @NotNull CrashPayload load(@NotNull Long key) {
                    return null;
                }
            });

    @Override
    public void onCreate() {
        super.onCreate();

        //For logging
        Timber.plant(new Timber.DebugTree() {
            @NonNull
            @Override
            protected String createStackElementTag(@NotNull StackTraceElement element) {
                return super.createStackElementTag(element) + " : " + element.getLineNumber();
            }
        });
    }

}
