package com.lol.vitalmonitor.crashhandler;

import com.lol.vitalmonitor.utils.Utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import timber.log.Timber;

public class CustomInterceptor implements Interceptor {

    CustomInterceptorNotifier customInterceptorNotifier;

    public CustomInterceptor(CustomInterceptorNotifier customInterceptorNotifier) {
        this.customInterceptorNotifier = customInterceptorNotifier;
    }

    public CustomInterceptor() {
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        StringBuilder requestSb = new StringBuilder();
        StringBuilder responseSb = new StringBuilder();
        long t1 = System.nanoTime();
        Timber.i("Sending request %s ", request.url());
        Timber.v("REQUEST BODY BEGIN\n%s\nREQUEST BODY END", bodyToString(request));

        requestSb.append("Sending request ")
                .append(request.url())
                .append(" REQUEST BODY BEGIN\n")
                .append(bodyToString(request))
                .append(" REQUEST BODY END");

        if(customInterceptorNotifier != null){
            customInterceptorNotifier.requestBody(requestSb.toString());
        }
        Response response = chain.proceed(request);

        ResponseBody responseBody = response.body();
        String responseBodyString = response.body().string();

        Response newResponse = response.newBuilder().body(ResponseBody.create(responseBody.contentType(), responseBodyString.getBytes())).build();

        long t2 = System.nanoTime();
        Timber.i("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers());
        Timber.v("RESPONSE BODY BEGIN:\n%s\nRESPONSE BODY END", responseBodyString);

        responseSb.append("Received response for ")
                .append(response.request().url())
                .append(" in ")
                .append((t2 - t1) / 1e6d)
                .append(" ms ")
                .append(response.headers())
                .append(responseBodyString);

        if(customInterceptorNotifier != null){
            customInterceptorNotifier.responseBody(responseSb.toString());
        }

        Utils.addAPIToMap(requestSb.toString(),responseSb.toString());

        return newResponse;
    }

    private static String bodyToString(final Request request){
        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            if(copy.body() != null){
                copy.body().writeTo(buffer);
            }
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "bodyToString failed";
        }
    }
}
