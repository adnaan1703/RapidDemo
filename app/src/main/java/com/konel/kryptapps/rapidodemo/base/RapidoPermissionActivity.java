package com.konel.kryptapps.rapidodemo.base;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.konel.kryptapps.rapidodemo.utils.AppConstantsUtil;
import com.konel.kryptapps.rapidodemo.R;
import com.konel.kryptapps.rapidodemo.utils.CodeUtil;
import com.konel.kryptapps.rapidodemo.utils.PermissionUtil;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 10 Aug 2017 10:50 PM
 */


public abstract class RapidoPermissionActivity extends AppCompatActivity implements PermissionUtil.PermissionAskListener {

    public static final int REQUEST_CODE_FOR_PERMISSION = 0x1;

    public void askedPermission(String permission) {
        getSharedPreferences(AppConstantsUtil.PREFERNCE_FILE_NAME, MODE_PRIVATE)
                .edit()
                .putBoolean(permission, true)
                .apply();
    }

    public boolean hasAskedPermission(String permission) {
        return getSharedPreferences(AppConstantsUtil.PREFERNCE_FILE_NAME, MODE_PRIVATE)
                .getBoolean(permission, false);
    }

    public boolean hasPermission(String permission) {
        return PermissionUtil.hasPermission(this, permission);
    }

    public boolean hasAllPermissions(String... permissions) {
        for (String permission : permissions) {
            if (!hasPermission(permission))
                return false;
        }
        return true;
    }

    public void askPermission(String permission) {
        PermissionUtil.checkPermission(this, permission, this);
    }

    public void askForAllPermissions(String... permissions) {
        PermissionUtil.checkAllPermission(this, permissions, this);
    }

    @Override
    public void onNeedPermission(final String[] permissions) {
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_FOR_PERMISSION);
    }

    @Override
    public void onPermissionPreviouslyDenied(final String permission) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_dialog_permission_request_text)
                .setTitle(R.string.alert_dialog_permission_request_title);
        builder.setPositiveButton(R.string.alert_dialog_permission_request_positive_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                askedPermission(permission);

            }
        });
        builder.setNegativeButton(R.string.alert_dialog_permission_request_negative_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                onPermissionDisabled();
            }
        });
    }

    @Override
    public void onPermissionDisabled() {
        CodeUtil.appSettingIntent(this);
        Toast.makeText(this, "Permission is disabled. Please give permission", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPermissionGranted() {
        // has the permission
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_FOR_PERMISSION) {
            if (grantResults.length == 0) {
                onPermissionDisabled();
                return;
            }

            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    onPermissionPreviouslyDenied(permissions[i]);
                    return;
                }
            }
            onPermissionGranted();
        }
    }
}
