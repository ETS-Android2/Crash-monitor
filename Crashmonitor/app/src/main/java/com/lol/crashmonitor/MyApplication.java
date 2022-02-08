package com.lol.crashmonitor;

import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.lol.crashmonitor.activities.ExceptionDisplayActivity;
import com.lol.vitalmonitor.crashhandler.CrashHandler;
import com.lol.vitalmonitor.crashhandler.CrashMonitor;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import io.sentry.Sentry;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

public class MyApplication extends MultiDexApplication implements Interceptor, CrashMonitor {
    private Thread.UncaughtExceptionHandler systemHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        // MultiDex initialization
        MultiDex.install(this.getApplicationContext());

        //For logging
        Timber.plant(new Timber.DebugTree() {
            @NonNull
            @Override
            protected String createStackElementTag(@NotNull StackTraceElement element) {
                return super.createStackElementTag(element) + " : " + element.getLineNumber();
            }
        });

        systemHandler = Thread.getDefaultUncaughtExceptionHandler();

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            //do noting
        });
        CrashHandler crashHandler = new CrashHandler(this);
        crashHandler.changeCrashThresholdLimit(4);
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        Timber.i("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers());
        Timber.v("REQUEST BODY BEGIN\n%s\nREQUEST BODY END", bodyToString(request));

        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();

        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

        long t2 = System.nanoTime();
        Timber.i("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers());
        Timber.v("RESPONSE BODY BEGIN:\n%s\nRESPONSE BODY END", responseBodyString);

        return newResponse;
    }

    private static String bodyToString(final Request request) {

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "bodyToString failed";
        }
    }

    @Override
    public void crashReport(Throwable exception) {
        StringWriter stackTrace = new StringWriter();
        exception.printStackTrace(new PrintWriter(stackTrace));
        StringBuilder errorReport = new StringBuilder();
        errorReport.append("************ CAUSE OF ERROR ************\n\n");
        errorReport.append(stackTrace.toString());

//        errorReport.append("\n************ DEVICE INFORMATION ***********\n");
//        errorReport.append("Brand: ");
//        errorReport.append(Build.BRAND);
//        String LINE_SEPARATOR = "\n";
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Device: ");
//        errorReport.append(Build.DEVICE);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Model: ");
//        errorReport.append(Build.MODEL);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Id: ");
//        errorReport.append(Build.ID);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Product: ");
//        errorReport.append(Build.PRODUCT);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("\n************ FIRMWARE ************\n");
//        errorReport.append("SDK: ");
//        errorReport.append(Build.VERSION.SDK);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Release: ");
//        errorReport.append(Build.VERSION.RELEASE);
//        errorReport.append(LINE_SEPARATOR);
//        errorReport.append("Incremental: ");
//        errorReport.append(Build.VERSION.INCREMENTAL);
//        errorReport.append(LINE_SEPARATOR);
    }

    @Override
    public void appRestart(boolean isRestart, @NotNull Thread thread, @NotNull Throwable exception) {
        sendCrashToReportingServer(thread, exception);
        if (!isRestart) {
            Intent intent = new Intent(getApplicationContext(), ExceptionDisplayActivity.class);
            intent.putExtra("error", "Seems like Unscripted has crashed restarting");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            getApplicationContext().startActivity(intent);
        } /*else {
            systemHandler.uncaughtException(thread, exception);
        }*/
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }

    private void sendCrashToReportingServer(@NotNull Thread thread, @NotNull Throwable exception) {
        FirebaseCrashlytics.getInstance().recordException(exception);
        Sentry.captureException(exception);
    }
}
