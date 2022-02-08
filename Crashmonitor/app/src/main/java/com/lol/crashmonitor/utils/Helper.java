package com.lol.crashmonitor.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static boolean isContainValue(String value) {
        return (value != null && !TextUtils.isEmpty(value) && !value.equalsIgnoreCase("null"));
    }

    public static boolean isNetworkAvailable(Context mContext) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null;
    }

    public static void generateKeyHAsh(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    static String parseCode(String message) {
        Pattern p = Pattern.compile("\\b\\d{4}\\b");
        Matcher m = p.matcher(message);
        String code = "";
        while (m.find()) {
            code = m.group(0);
        }
        return code;
    }

    public static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isEmail(String text) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern p = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        return m.matches();
    }

    public static boolean isPhone(String text) {
        if (!TextUtils.isEmpty(text)) {
            return TextUtils.isDigitsOnly(text);
        } else {
            return false;
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }

        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static String getCurrentDates() {
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return simpleDateFormat.format(date);
    }

    public static String getAppVersionNumber(Context context) {
        String verCode = "";
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
                verCode = pInfo.getLongVersionCode() + "";
            }else {
                verCode = pInfo.versionCode + "";
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verCode;
    }

    static boolean isDebuggableMode(Context context) {
        return (0 != (context.getApplicationInfo().flags &= ApplicationInfo.FLAG_DEBUGGABLE));
    }

    public static boolean isRotationOn(Context context) {
        return (Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION, 0) == 1);
    }

    public static long getDateDiff(long timeUpdate, long timeNow, TimeUnit timeUnit) {
        long diffInMillis = timeNow - timeUpdate;
        return timeUnit.convert(diffInMillis, TimeUnit.MILLISECONDS);
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

    public static float convertDpToPixel(Context context, float dp) {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(Context context, float px) {
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float pxFromDp(final Context context, final float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float scale = displayMetrics.density;
        return (dp * scale + 0.5f);
    }

    public static String getDensityName(Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            return "xxxhdpi";
        }
        if (density >= 3.0) {
            return "xxhdpi";
        }
        if (density >= 2.0) {
            return "xhdpi";
        }
        if (density >= 1.5) {
            return "hdpi";
        }
        if (density >= 1) {
            return "mdpi";
        }
        return "ldpi";
    }

    public static void postDelayThreeSecond(final View v) {
        v.setClickable(false);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setClickable(true);
            }
        }, 3000);
    }

    /**
     * method used to generate premium video filter for elastic search,
     * this method will not be called if app is running in "free mode" i.e
     * if AppConstants.APP_MODE is set to free in preference.
     * @param offset
     * @return
     */
    public static String getPremiumVideoFilter(String offset) {
        return "{\"order\": [\"pub_date DESC\"], \"where\": {\"meta_data.premium\": true}, \"limit\": 8, \"offset\":" +
                offset +
                "}";
    }

    public static String getLatestVideoFilter(String offset,boolean isPremiumMode) {
        return "{\"order\": [\"pub_date DESC\"],  \"where\": {\"meta_data.premium\": " +
                isPremiumMode +
                "}, \"limit\": 8, \"offset\":" +
                offset +
                "}";
    }

    public static String getRecommendedVideoFilter(String offset, String slug,boolean isPremiumMode) {
        return "{\"order\": [\"pub_date DESC\"], \"where\": {\"meta_data.premium\": " +
                isPremiumMode +
                ",\"slug\": {\"nin\": [\"" +
                slug +
                "\"]}}, \"limit\": 8, \"offset\": " +
                offset +
                " }";
    }

    public static String getShowsFilter(String offset) {
        return "{\"order\": [\"last_video_pub_epoch_date DESC\"], \"limit\": 8, \"offset\":" +
                offset +
                "}";
    }

    public static String getShowVideoFilter(String offset, String slug,boolean isPremiumMode) {
        return "{\"order\": [\"pub_date DESC\"], \"where\": {\"meta_data.premium\": " +
                isPremiumMode +
                ",\"show.show_slug\": \"" +
                slug +
                "\"} ,\"limit\": 8, \"offset\":" +
                offset +
                "}";
    }


    public static String getAutoPlayVideoFilter(boolean premium, String showSlug, String slug) {
        if(premium){
            return "{\"order\": [\"pub_date DESC\"], \"limit\": 1, \"offset\": 0, \"where\": {\"show.show_slug\":\"" +
                    showSlug +
                    "\",\"slug\": {\"nin\":[\"" +
                    slug +
                    "\"]}}}";
        }else {
            return "{\"order\": [\"pub_date DESC\"], \"limit\": 1, \"offset\": 0, \"where\": {\"meta_data.premium\": " +
                    premium +
                    ", \"show.show_slug\":\"" +
                    showSlug +
                    "\",\"slug\": {\"nin\":[\"" +
                    slug +
                    "\"]}}}";
        }
    }
}