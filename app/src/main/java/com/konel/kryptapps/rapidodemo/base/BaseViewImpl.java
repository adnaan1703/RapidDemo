package com.konel.kryptapps.rapidodemo.base;

import android.content.Context;

/**
 * @author : Adnaan 'Zohran' Ahmed <adnaanahmed@urbanclap.com>
 * @version : 1.0.0
 * @since : 11 Aug 2017 1:04 PM
 */


public interface BaseViewImpl {
    Context getViewContext();

    void showError(String message);
}
