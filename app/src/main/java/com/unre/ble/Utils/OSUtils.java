package com.unre.ble.Utils;
//
// Created by JasWorkSpace on 2019/1/28.
// Copyright (c) 2019 Pinocchio_Android All rights reserved.

import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

public class OSUtils {
    public static boolean isInPackageProcess(Context context){
        return TextUtils.equals(context.getPackageName(), getCurrentProcessName(context));
    }
    public static String getCurrentProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
    public static String debugPaths(Context context){
        String result = context.getPackageName()
                + ", getPackageCodePath:" + context.getPackageCodePath()
                + ", getPackageResourcePath:" + context.getPackageResourcePath()
                + ", getCacheDir:" + context.getCacheDir().getAbsolutePath()
                + ", getCodeCacheDir:" + context.getCodeCacheDir().getAbsolutePath()
                + ", getFilesDir:" + context.getFilesDir().getAbsolutePath()
                + ", getNoBackupFilesDir:" + context.getNoBackupFilesDir().getAbsolutePath();
        if(context.getExternalCacheDir() != null){
            result += (", getExternalCacheDir:" + context.getExternalCacheDir().getAbsolutePath());
        }
        if(Build.VERSION.SDK_INT >= 24) {
            result += (", getDataDir:" + context.getDataDir().getAbsolutePath());
        }
        return result;
    }
    public static int getAPPVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return versionCode;
    }
    public static String getAPPVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return verName;
    }
}
