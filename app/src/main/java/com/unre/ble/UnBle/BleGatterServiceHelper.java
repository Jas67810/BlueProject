package com.unre.ble.UnBle;
//
// Created by JasWorkSpace on 2019/2/17.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import static android.content.Context.BIND_AUTO_CREATE;
/// 该帮助文件可以写成单利模式
public class BleGatterServiceHelper{
    private final static String TAG = BleGatterServiceHelper.class.getSimpleName();
    private Context context;
    public BleGatterServiceHelper(Context context){
        this.context = context.getApplicationContext();
    }
    public BleGatterService getBleGatterService() {
        if(bleGatterService == null){
            bindOrUnbindBleGatterService(needBind);
        }
        return bleGatterService;
    }
    ///////////////////////////////////////////////
    private volatile BleGatterService bleGatterService;
    private BleGatterServiceConnect mBleGatterServiceConnect;
    private boolean needBind = false;
    public synchronized void bindBleGatterService(){
        needBind = true;
        bindOrUnbindBleGatterService(true);
    }
    public synchronized void unbindBleGatterService(){
        needBind = false;
        bindOrUnbindBleGatterService(false);
    }
    //////////////////////////////////////////////////////////
    private synchronized boolean bindOrUnbindBleGatterService(boolean bind){
        if(bind){
            if(needBind){
                if(mBleGatterServiceConnect == null) {
                    context.bindService(new Intent(context, BleGatterService.class),
                            new BleGatterServiceConnect(), BIND_AUTO_CREATE);
                    return true;
                }
            }
        } else {
            if(!needBind){
                if(mBleGatterServiceConnect != null) {
                    context.unbindService(mBleGatterServiceConnect);
                    return true;
                }
            }
        }
        return false;
    }
    private class BleGatterServiceConnect implements ServiceConnection{
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG,"------ onServiceConnected ------");
            mBleGatterServiceConnect = this;
            bleGatterService = ((BleGatterService.UnBleGatterServiceBinder)service).getUnBleGatterService();
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"------ onServiceDisconnected ------");
            bleGatterService = null;
            mBleGatterServiceConnect = null;
            bindOrUnbindBleGatterService(needBind);
        }
    }
    ////////////////////////////////////////////////////////////////////////
}
