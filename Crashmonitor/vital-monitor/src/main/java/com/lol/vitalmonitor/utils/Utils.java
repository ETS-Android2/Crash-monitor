package com.lol.vitalmonitor.utils;

import com.lol.vitalmonitor.VitalMonitorApplication;
import com.lol.vitalmonitor.model.CrashPayload;
import com.lol.vitalmonitor.model.NetworkCallData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentMap;

import timber.log.Timber;

public class Utils {

    public static long getCurrentUTCTimestamp() {
        long dateInMillis = System.currentTimeMillis();
        dateInMillis = dateInMillis / 1000;
        return dateInMillis;
    }

    public static void addAPIToMap(String request, String response) {
        long currentUTCTimestamp = getCurrentUTCTimestamp();
        NetworkCallData networkCallData = new NetworkCallData(currentUTCTimestamp, request, response);
        VitalMonitorApplication.networkCallDataLoadingCache.put(currentUTCTimestamp, networkCallData);
    }

    public static ArrayList<NetworkCallData> getAPIMapToList() {
        ArrayList<NetworkCallData> networkCallDataList = new ArrayList<>();
        ConcurrentMap<Long, NetworkCallData> networkCallDataConcurrentMap = VitalMonitorApplication.networkCallDataLoadingCache.asMap();
        for (Long key : networkCallDataConcurrentMap.keySet()) {
            Timber.i("key : %s", key);
            Timber.i("value : %s", networkCallDataConcurrentMap.get(key));
            networkCallDataList.add(networkCallDataConcurrentMap.get(key));
        }
        return networkCallDataList;
    }

    public static void addCrashToMap(Throwable exception, String stackTrace) {
        long currentUTCTimestamp = getCurrentUTCTimestamp();
        CrashPayload crashPayload = new CrashPayload("", currentUTCTimestamp, stackTrace, exception.toString(), getAPIMapToList());
        VitalMonitorApplication.crashDataLoadingCache.put(currentUTCTimestamp, crashPayload);
    }

    public static ArrayList<CrashPayload> getCrashMapToList() {
        ArrayList<CrashPayload> crashPayloadsList = new ArrayList<>();
        ConcurrentMap<Long, CrashPayload> crashPayloadCurrentMap = VitalMonitorApplication.crashDataLoadingCache.asMap();
        for (Long key : crashPayloadCurrentMap.keySet()) {
            Timber.i("key : %s", key);
            Timber.i("value : %s", crashPayloadCurrentMap.get(key));
            crashPayloadsList.add(crashPayloadCurrentMap.get(key));
        }
        return crashPayloadsList;
    }

    public static long getTimeInMillisecondsFromDate(String givenDateString) {
        String dateFormat = "yyyy-MM-dd kk:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        try {
            Date mDate = sdf.parse(givenDateString);
            return mDate.getTime();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

}