package com.unre.ble.UnBle;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.unre.ble.EventBus.EventID;
import com.unre.ble.EventBus.EventUtils;
import com.unre.unreble.Common.UnBleManager;
import com.unre.unreble.Service.UnBleGatterService;
import com.unre.unreble.Service.UnBleServiceListener;
import java.util.Arrays;
import java.util.Set;

public class BleGatterService extends BleService implements UnBleServiceListener {
    private final static String TAG = BleGatterService.class.getSimpleName();
    public class UnBleGatterServiceBinder extends Binder{
        public BleGatterService getUnBleGatterService(){
            return BleGatterService.this;
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new UnBleGatterServiceBinder();
    }
    @Override
    public void onDestroy() {
        if(unBleGatterService != null){
            unBleGatterService.setUnBleServiceListener(null);
            unBleGatterService.teardown();
            unBleGatterService = null;
        }
        super.onDestroy();
    }
    /////////////////////////////////////////////////////////
    @Override
    protected void handleBleMessage(Message msg) {
        super.handleBleMessage(msg);
        switch (msg.what){
            case BLE_MSG_INIT:{
                unBleGatterService = UnBleManager.getBleGatterService(this);
                unBleGatterService.setUnBleServiceListener(this);
                unBleGatterService.setup();//启动
            }break;
        }
    }
    ///////////////////////////////////////////////////
    private UnBleGatterService unBleGatterService;
    @Override
    public void onCharacteristicWrite(int offset, byte[] value) {
        Log.d(TAG,"onCharacteristicWrite(" + offset + ", " + Arrays.toString(value) + ")");
        EventUtils.postEvent(true, EventID.EVENTID_BLE_RECEIVER_DATA, value);
        BleDataParser.parserUnBleData(offset,value);
    }
    @Override
    public void onRegisteredDevicesChange() {
        Log.d(TAG, "onRegisteredDevicesChange:" + unBleGatterService.debugRegisteredDevices());
        EventUtils.postEvent(true, EventID.EVENTID_BLE_RECEIVER_DEVICESCHANGE, 0);
    }
    ///////////////////////////////
    // API
    public Set<BluetoothDevice> getCurrentRegisteredDevices(){
        if(unBleGatterService != null){
           return unBleGatterService.getCurrentRegisteredDevices();
        }
        return null;
    }
}
