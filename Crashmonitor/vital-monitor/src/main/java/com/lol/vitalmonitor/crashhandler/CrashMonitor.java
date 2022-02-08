package com.lol.vitalmonitor.crashhandler;

import org.jetbrains.annotations.NotNull;

public interface CrashMonitor {
    void crashReport(Throwable exception);
    void appRestart(boolean isRestart, @NotNull Thread thread, @NotNull Throwable exception);
}
