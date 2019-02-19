package com.unre.ble.UnBle.Client;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import com.inuker.bluetooth.library.BluetoothClient;
import com.unre.ble.UnBle.BaseServiceHelper;

public class BleClientServiceHelper extends BaseServiceHelper {
    private final static String TAG = BleClientServiceHelper.class.getSimpleName();
    private volatile BleClientService bleClientService;
    public BleClientServiceHelper(Context context){
        super(context);
        serviceClass = BleClientService.class;
    }
    public BleClientService getBleClientService(){
        if(bleClientService == null){
            bindOrUnbindService();
        }
        return bleClientService;
    }
    public BluetoothClient getBluetoothClient(){
        if(bleClientService != null){
            return bleClientService.getBluetoothClient();
        }
        return null;
    }
    /////////////////////////////////////////////////////////////////////
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        bleClientService = ((BleClientService.BleClentServiceBinder)service).getBleClentService();
        super.onServiceConnected(name, service);
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        bleClientService = null;
        super.onServiceDisconnected(name);
    }
    /////////////////////////////////////////////////////////////////////////
}
