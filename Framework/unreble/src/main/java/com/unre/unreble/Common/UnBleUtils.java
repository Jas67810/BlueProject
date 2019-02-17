package com.unre.unreble.Common;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import static android.content.Context.BLUETOOTH_SERVICE;

public class UnBleUtils {
    private final static String TAG = UnBleUtils.class.getSimpleName();
    public static boolean checkBluetoothSupport(Context context) {
        if (((BluetoothManager) context.getSystemService(BLUETOOTH_SERVICE)).getAdapter() == null) {
            Log.w(TAG, "Bluetooth is not supported");
            return false;
        }
        if (!context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Log.w(TAG, "Bluetooth LE is not supported");
            return false;
        }
        return true;
    }
}
