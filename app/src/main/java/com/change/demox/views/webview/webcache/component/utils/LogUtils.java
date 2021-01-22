package com.change.demox.views.webview.webcache.component.utils;

import timber.log.Timber;

/**
 * Created by Ryan
 * at 2019/10/8
 */
public class LogUtils {

    private static final String TAG = "FastWebView";
    public static boolean DEBUG = false;

    public static void d(String message) {
        if (DEBUG) {
            Timber.d(message);
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Timber.e(message);
        }
    }
}
