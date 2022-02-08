package com.lol.crashmonitor.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.lol.crashmonitor.R;
import com.lol.vitalmonitor.crashhandler.CrashActivityBackButtonPress;
import com.lol.vitalmonitor.ui.ErrorDisplayFragment;

import timber.log.Timber;

public class ExceptionDisplayActivity extends AppCompatActivity implements CrashActivityBackButtonPress {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exception_display_activity);

        ErrorDisplayFragment errorDisplayFragment = new ErrorDisplayFragment(this);
        FragmentManager ft = getSupportFragmentManager();
        ft.beginTransaction().add(R.id.frame_container, errorDisplayFragment,"errorDisplayFragment").commitAllowingStateLoss();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getIntent() != null
                        && getIntent().getExtras() != null
                        && getIntent().getExtras().getString("error") != null){
                    errorDisplayFragment.setErrorText(getIntent().getExtras().getString("error"));
                }else {
                    errorDisplayFragment.setErrorText("Seems like Unscripted has crashed restarting");
                }
            }
        },100);

    }

    @Override
    public void onBackPressed() {
        intentData();
    }

    public void intentData() {
        Timber.d("CDA onBackPressed Called");
        Intent setIntent = new Intent(ExceptionDisplayActivity.this, MainActivity.class);
        setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(setIntent);
        finish();
    }

    @Override
    public void backButtonPress() {
        intentData();
    }
}
