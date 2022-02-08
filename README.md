# Crash-monitor

Initially created as an application wrapper disabling system's Thread.UncaughtExceptionHandler and showing a UI in case of the app crashing.
It has been now changed to a monitoring/reporting + trying to elegantly hide android crashes.

Crash Monitor
Whenever the android app crashes callbacks are triggered and the application is restarted and the crash is sent to reporting server. On the basis of crash frequency, the user can decide what to do. Callback appRestart provides a parameter named isRestart stating if the app needs to be closed or crash UI should be shown to the user.

Adding to project

Disable system's uncaught exception handler Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> { //do noting });
Add CrashMonitor interface as an implementation to the main app's application class.
Crash report and should app be restarted is send using crashReport(Throwable e) and appRestart(boolean isRestart, @NotNull Thread thread, @NotNull Throwable exception)
Initialize CrashHandler class CrashHandler crashHandler = new CrashHandler(this); in main app's application class.
Set crashHandler as the default uncaught exception handler Thread.setDefaultUncaughtExceptionHandler(crashHandler);
Crash threshold can be set using changeCrashThresholdLimit(int crashThreshold) by default it is 2.

API monitor
Network calls can also be tracked and are cached if implemented, which can also be sent to the reporting server as a crash log.
Network tracking only supports retrofit implementations for now.

Adding to project

Add CustomInterceptor to OkHttpClient CustomInterceptor customInterceptor = new CustomInterceptor(); and builder.addInterceptor(customInterceptor);
