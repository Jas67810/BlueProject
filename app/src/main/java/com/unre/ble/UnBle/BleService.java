package com.unre.ble.UnBle;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.app.Service;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public abstract class BleService extends Service {
    ////////////////////////////////////////////////////
    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread(BleService.class.getSimpleName(), android.os.Process.THREAD_PRIORITY_FOREGROUND);
        mHandlerThread.start();
        mBleHandler = new BleHandler(mHandlerThread.getLooper());
        mBleHandler.sendEmptyMessage(BLE_MSG_INIT);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mHandlerThread.quitSafely();
    }
    ///////////////////////////////////////////////////////
    private HandlerThread mHandlerThread;
    protected BleHandler mBleHandler;
    private class BleHandler extends Handler{
        BleHandler(Looper looper){
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handleBleMessage(msg);
        }
    }
    protected void handleBleMessage(Message msg){

    }
    ////////////////////////////////////////////
    protected final static int BLE_MSG_INIT = 1;
    protected final static int BLE_MSG_BASE = 100;
}
