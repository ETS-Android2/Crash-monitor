package com.lol.crashmonitor.network;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.stream.MalformedJsonException;
import com.lol.crashmonitor.model.base.BaseResponse;
import com.lol.crashmonitor.utils.Helper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import timber.log.Timber;

public abstract class NetworkHandler<T> implements Callback<T> {

    private final Context context;
    private final MasterView masterView;

    public NetworkHandler(Context context, MasterView masterView) {
        this.context = context;
        this.masterView = masterView;
    }

    /**
     * Override this method in your class for handling success response
     *
     * @param request  request called
     * @param response response received
     * @return boolean (whether you have handled its response in your class or
     * not)
     */
    public boolean handleResponse(Request request, BaseResponse response) {
        return false;
    }

    /**
     * Method for handling errors. Override this method in your class for
     * handling error
     *
     * @param request request called
     * @param error   error received from api
     * @return boolean (whether you have handled its response in your class or
     * not)
     */
    public boolean handleError(Request request, Error error) {
        return false;
    }

    /**
     * Method for handling failure response Override this method for failure
     * action
     *
     * @param request request called
     * @param ex      failure exception
     * @param message
     * @return boolean (handled or not)
     */
    public boolean handleFailure(Request request, Exception ex, String message) {
        return false;
    }

    /**
     * Override method from {@link Callback}, will be called when successful
     * response will come from server
     *
     * @param call     api call request
     * @param response response received
     */
    @Override
    public final void onResponse(@NonNull Call<T> call, @NonNull Response<T> response) {
        // checking whether response was successful else throw failure
        if (isRequestSuccessful(response) && masterView != null) {
            BaseResponse baseResponse = (BaseResponse) response.body();
            if (!handleResponse(call.request(), baseResponse)) {
                if (baseResponse != null) {
                    Timber.e("Request: (%s), Response not handled: (%s",
                            call.request().url(), baseResponse.toString());
                }
            }
        } else {
            Throwable error;
            try {
                error = new ErrorUtils.ApiErrorException(response.errorBody().string());
            } catch (IOException e) {
                e.printStackTrace();
                error = e;
            }
            onFailure(call, error);
        }
    }

    /**
     * Override method from {@link Callback}, will be called when api failure
     * has occurred
     *
     * @param call      api call requested
     * @param throwable error as {@link Throwable}
     */
    @Override
    public final void onFailure(@NotNull Call<T> call, @NotNull Throwable throwable) {
        // check view available then proceed
        if (masterView != null && context != null) {
            Exception exception = new Exception(throwable);
            // check whether network available or not
            if (exception.getCause() instanceof UnknownHostException) {
                Timber.e("Unable to connect. Please make sure you are connected to a network.");
//                masterView.showMessage("Unable to connect. Please make sure you are connected to a network.");
            }
            // check whether server sent valid json format
            else if (exception.getCause() instanceof MalformedJsonException) {
                Timber.e("Server sent an invalid response. Please try again later.");
//                masterView.showMessage("Server sent an invalid response. Please try again later.");
            }
            // check for custom api exception
            else if (exception.getCause() instanceof ErrorUtils.ApiErrorException) {
                Timber.e("Server sent an invalid response. Please try again later.");
//                masterView.showMessage("Server sent an invalid response. Please try again later.");
            }
            // check for time out exception
            else if (exception.getCause() instanceof SocketTimeoutException) {
                Timber.e("Timeout occurred. Please try again later.");
//                masterView.showMessage("Timeout occurred. Please try again later.");
            }
            // send callback to {@link handleFailure}
            String message = "";
            try {
                if (Helper.isContainValue(throwable.getMessage())) {
                    JSONObject jsonObject = new JSONObject(throwable.getMessage());
                    message = jsonObject.optString("Message");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (!handleFailure(call.request(), exception, message)) {
                Timber.e("Request: (%s), Failure not handled: (%s",
                        call.request().url(), throwable.getMessage());
            }
        }
    }

    private boolean isRequestSuccessful(Response<T> response) {
        return response != null && response.isSuccessful() && response.errorBody() == null;
    }
}