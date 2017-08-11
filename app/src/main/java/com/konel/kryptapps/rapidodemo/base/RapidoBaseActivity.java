package com.konel.kryptapps.rapidodemo.base;

import android.widget.Toast;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 1:51 AM
 */


public abstract class RapidoBaseActivity extends RapidoLocationActivity {

    protected void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    protected void showToast(String format, Object... args) {
        showToast(String.format(format, args));
    }

}
