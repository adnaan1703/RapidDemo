package com.konel.kryptapps.rapidodemo.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 09 Aug 2017 10:33 PM
 */


public class AppConstantsUtil {

    public static final String GOOGLE_DIRECTIONS_API_KEY = "AIzaSyC1CJOkBgODappNCwK52diZrUyTNzl3TJk";
    public static final String PREFERNCE_FILE_NAME = "rapido_demo_preference";

    public static String getGoogleServicesApiKey(Context context) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = applicationInfo.metaData;
            return bundle.getString("com.google.android.geo.API_KEY");
        } catch (PackageManager.NameNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }
}
