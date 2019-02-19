package com.unre.ble.UnBle;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;

/// 该帮助文件可以写成单利模式
public class BleGatterServiceHelper extends BaseServiceHelper{
    private final static String TAG = BleGatterServiceHelper.class.getSimpleName();
    private volatile BleGatterService bleGatterService;
    public BleGatterServiceHelper(Context context){
        super(context);
        serviceClass = BleGatterService.class;
    }
    public BleGatterService getBleGatterService() {
        if(bleGatterService == null){
            bindOrUnbindService();
        }
        return bleGatterService;
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        bleGatterService = null;
        super.onServiceDisconnected(name);

    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        bleGatterService = ((BleGatterService.BleGatterServiceBinder)service).getBleGatterService();
        super.onServiceConnected(name, service);
    }
    ////////////////////////////////////////////////////////////////////////
    //public interface
}
