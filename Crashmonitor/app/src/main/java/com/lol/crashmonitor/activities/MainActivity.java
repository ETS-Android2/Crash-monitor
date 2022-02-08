package com.lol.crashmonitor.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.SpinKitView;
import com.lol.crashmonitor.R;
import com.lol.crashmonitor.model.constant.ConstantsResponse;
import com.lol.crashmonitor.network.NetworkManager;
import com.lol.crashmonitor.utils.Helper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.sentry.Sentry;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private SpinKitView spinKit;
    private TextView apiText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button crashButton = new Button(this);
        crashButton.setText("Test Crash");
        crashButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                throw new RuntimeException("Test Crash"); // Force a crash
            }
        });

        addContentView(crashButton, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        context = this;
        apiText = findViewById(R.id.apiText);
        spinKit = findViewById(R.id.spinKit);

        findViewById(R.id.crashButton).setOnClickListener(view -> {
            throw new NullPointerException();
        });

        findViewById(R.id.hitApi).setOnClickListener(view -> {
            spinKit.setVisibility(View.VISIBLE);
            apiText.setText("");
            NetworkManager.getConstantsApi().getConstants("12")
                    .enqueue(new Callback<ArrayList<ConstantsResponse>>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onResponse(@NotNull Call<ArrayList<ConstantsResponse>> call
                        , @NotNull Response<ArrayList<ConstantsResponse>> response) {
                    try {
                        spinKit.setVisibility(View.GONE);
                        if (response.body() != null
                                && response.body().size() > 0) {
                            if (Helper.isContainValue(response.body().get(0).getAppMode())
                                    && Helper.isContainValue(response.body().get(0).getRazorPayKey())) {
                                apiText.append("\n API RESPONSE \n");
                                apiText.append("App mode : " + response.body().get(0).getAppMode()
                                        + "\n RazorPay key : " +response.body().get(0).getRazorPayKey() + "\n");
                            } else {
                                showErrorDialog(context.getResources().getString(R.string.please_check_internet_connection), "");
                            }
                        } else {
                            showErrorDialog(context.getResources().getString(R.string.please_check_internet_connection), "");
                        }
                    } catch (Exception e) {
                        spinKit.setVisibility(View.GONE);
                        showErrorDialog(context.getResources().getString(R.string.please_check_internet_connection), "");
                    }
                }

                @Override
                public void onFailure(@NotNull Call<ArrayList<ConstantsResponse>> call, @NotNull Throwable t) {
                    try {
                        spinKit.setVisibility(View.GONE);
                        showErrorDialog(t.getMessage(), "");
                    } catch (Exception e) {
                        spinKit.setVisibility(View.GONE);
                        e.printStackTrace();
                    }

                }
            });
        });
    }

    private void showErrorDialog(String message, String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message)
                .setCancelable(false);

        if (type.equalsIgnoreCase("force_update")) {
            builder.setPositiveButton("Update", (dialog, which) -> {
                final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="
                            + appPackageName)));
                }
            });
        } else {
            builder.setPositiveButton("Ok", (dialog, which) -> {
                dialog.cancel();
                finish();
            });
        }

        AlertDialog alert = builder.create();
        alert.show();
    }

//    @Override
//    public void requestBody(String request) {
//        Timber.i(request);
//        runOnUiThread(() -> apiText.append("\n REQUEST BODY \n "+request + " \n"));
//    }
//
//    @Override
//    public void responseBody(String response) {
//        Timber.i(response);
//        runOnUiThread(() -> apiText.append("\n RESPONSE BODY \n "+response + " \n"));
//    }
}