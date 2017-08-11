package com.konel.kryptapps.rapidodemo.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 10 Aug 2017 7:23 PM
 */


public class CodeUtil {

    @Nullable
    public static String LatLngToString(@Nullable LatLng latLng) {
        if (latLng == null)
            return null;
        return String.format(Locale.getDefault(), "%f,%f", latLng.latitude, latLng.longitude);
    }

    public static boolean isEmptyOrNull(List list) {
        return list == null || list.size() == 0;
    }

    public static int dpToPx(Context context, double dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp);
    }

    public static void appSettingIntent(AppCompatActivity activity) {
        Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        List<ResolveInfo> resolveInfoList = activity.getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (null == resolveInfoList || resolveInfoList.isEmpty()) {
            return;
        }
        activity.startActivity(intent);
    }
}
