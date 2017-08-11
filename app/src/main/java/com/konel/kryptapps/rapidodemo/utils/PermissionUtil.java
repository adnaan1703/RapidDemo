package com.konel.kryptapps.rapidodemo.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v13.app.ActivityCompat;

import com.konel.kryptapps.rapidodemo.base.RapidoPermissionActivity;

import java.util.ArrayList;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 10 Aug 2017 11:02 PM
 */

public class PermissionUtil {

    public static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    private static boolean shouldAskPermission(Context context, String permission) {
        if (shouldAskPermission()) {
            int permissionResult = ActivityCompat.checkSelfPermission(context, permission);
            if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    public static boolean hasPermission(Context context, String permission) {
        return !shouldAskPermission(context, permission);
    }

    public static void checkPermission(RapidoPermissionActivity activity, String permission, PermissionAskListener listener) {
        if (shouldAskPermission(activity, permission)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                listener.onPermissionPreviouslyDenied(permission);
            } else {

                if (!activity.hasAskedPermission(permission)) {
                    activity.askedPermission(permission);
                    listener.onNeedPermission(new String[]{permission});
                } else {
                    listener.onPermissionDisabled();
                }
            }
        } else {
            listener.onPermissionGranted();
        }
    }

    public static void checkAllPermission(RapidoPermissionActivity activity, String[] permissions, PermissionAskListener listener) {
        if (shouldAskAnyPermission(activity, permissions)) {
            String[] filteredPermissions = getRequiredPermissions(activity, permissions);
            if (filteredPermissions.length > 0)
                listener.onNeedPermission(filteredPermissions);
            else
                listener.onPermissionDisabled();
        } else {
            listener.onPermissionGranted();
        }
    }

    private static String[] getRequiredPermissions(RapidoPermissionActivity activity, String[] permissions) {
        ArrayList<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (shouldAskPermission(activity, permission) && !ActivityCompat.shouldShowRequestPermissionRationale(activity, permission))
                permissionList.add(permission);
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

    private static boolean shouldAskAnyPermission(RapidoPermissionActivity activity, String[] permissions) {
        for (String permission : permissions) {
            if (shouldAskPermission(activity, permission))
                return true;
        }
        return false;
    }

    public interface PermissionAskListener {

        void onNeedPermission(String[] permissions);

        void onPermissionPreviouslyDenied(String permission);

        void onPermissionDisabled();

        void onPermissionGranted();
    }
}

