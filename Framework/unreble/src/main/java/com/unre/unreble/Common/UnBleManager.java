package com.unre.unreble.Common;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Context;

import com.inuker.bluetooth.library.BluetoothContext;
import com.unre.unreble.Service.UnBleGatterService;

public class UnBleManager {

    public static void UnBleManager(Context context){
        BluetoothContext.set(context.getApplicationContext());
    }
    ////////////////////////////
    private volatile static UnBleGatterService unBleGatterService;
    public static UnBleGatterService getBleGatterService(Context context){
        if(unBleGatterService == null){
            synchronized (UnBleManager.class){
                if(unBleGatterService == null){
                    unBleGatterService = new UnBleGatterService(context);
                }
            }
        }
        return unBleGatterService;
    }
}
