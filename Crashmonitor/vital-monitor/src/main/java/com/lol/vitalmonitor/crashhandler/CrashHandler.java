package com.lol.vitalmonitor.crashhandler;

import android.os.Build;

import com.lol.vitalmonitor.model.CrashPayload;
import com.lol.vitalmonitor.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;

import timber.log.Timber;

public class CrashHandler implements
        Thread.UncaughtExceptionHandler {
    private final CrashMonitor crashMonitor;
    private int crashThreshold = 2; //default

    public CrashHandler(CrashMonitor crashMonitor) {
        this.crashMonitor = crashMonitor;
    }

    public void changeCrashThresholdLimit(int crashThreshold) {
        this.crashThreshold = crashThreshold;
    }

    public void uncaughtException(@NotNull Thread thread, @NotNull Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
        errorReport.append("Brand: ");
        errorReport.append(Build.BRAND);
        String LINE_SEPARATOR = "\n";
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Device: ");
        errorReport.append(Build.DEVICE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Model: ");
        errorReport.append(Build.MODEL);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Id: ");
        errorReport.append(Build.ID);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Product: ");
        errorReport.append(Build.PRODUCT);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("\n************ FIRMWARE ************\n");
        errorReport.append("SDK: ");
        errorReport.append(Build.VERSION.SDK);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Release: ");
        errorReport.append(Build.VERSION.RELEASE);
        errorReport.append(LINE_SEPARATOR);
        errorReport.append("Incremental: ");
        errorReport.append(Build.VERSION.INCREMENTAL);
        errorReport.append(LINE_SEPARATOR);

        crashMonitor.crashReport(exception);

        Utils.addCrashToMap(exception, errorReport.toString());

        ArrayList<CrashPayload> crashPayloads = Utils.getCrashMapToList();
        int tempCrashThreshold = 0;
        boolean isRestart = false;

        for (int i = 0; i < crashPayloads.size(); i++) {
            if (i + 1 >= crashPayloads.size()) {
                isRestart = false;
                break;
            }
            if (crashPayloads.get(i).getCrashCause().equalsIgnoreCase(crashPayloads.get(i + 1).getCrashCause())) {
                tempCrashThreshold++;
                if (tempCrashThreshold == crashThreshold - 1) {
                    //crashes were same and have reaches crash threshold handover to ATR
                    isRestart = true;
                    break;
                } else {
                    //crashes are same but list is incomplete so run for loop further
                    //check if last item was checked and still no match then exit
                    //this will ensure no 2 crashes were consecutive
                    if (i == crashPayloads.size() - 1) {
                        isRestart = false;
                    }
                }
            } else {
                //crashes were different don't handover to ATR
                isRestart = false;
                break;
            }
        }

        crashMonitor.appRestart(isRestart,thread, exception);
    }

}
