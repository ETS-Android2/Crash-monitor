package com.lol.vitalmonitor.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.lol.vitalmonitor.crashhandler.CrashHandler;
import com.lol.vitalmonitor.crashhandler.CrashMonitor;

import org.jetbrains.annotations.NotNull;

/**
 * Static activity to override and add to a specific activity
 */
public class ErrorHandlerActivity extends AppCompatActivity implements CrashMonitor {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(this));
    }

    @Override
    public void crashReport(Throwable exception) {

    }

    @Override
    public void appRestart(boolean isFirst, @NotNull Thread thread, @NotNull Throwable exception) {

    }
}
