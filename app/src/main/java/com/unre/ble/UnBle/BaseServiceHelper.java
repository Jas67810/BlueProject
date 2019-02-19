package com.unre.ble.UnBle;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.unre.ble.UnBle.Client.BleClientServiceHelper;

import static android.content.Context.BIND_AUTO_CREATE;

public class BaseServiceHelper implements ServiceConnection{
    private final static String TAG = BaseServiceHelper.class.getSimpleName();
    public Context context;
    private boolean needBind = false;
    private boolean serviceConnected = false;
    public Class serviceClass = null;
    public BaseServiceHelper(Context context){
        this.context = context.getApplicationContext();
    }
    public void bindService(){
        needBind = true;
        bindOrUnbindService();
    }
    public void unBindService(){
        needBind = false;
        bindOrUnbindService();
    }
    //////////////////////////////////////////////////////////////////
    protected boolean bindOrUnbindService(){
        return bindOrUnbindService(needBind, serviceClass);
    }
    private synchronized boolean bindOrUnbindService(boolean bind, Class service){
        if(bind){
            if(needBind){
                if(!serviceConnected){
                    context.bindService(new Intent(context, service), this, BIND_AUTO_CREATE);
                    return true;
                }
            }
        } else {
            if(!needBind){
                if(serviceConnected){
                    context.unbindService(this);
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.d(TAG,"------ onServiceConnected ------\n" + name.flattenToString());
        serviceConnected = true;
        notifyServiceConnectionChange(true);
    }
    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.d(TAG,"------ onServiceDisconnected ------\n" + name.flattenToString());
        serviceConnected = false;
        bindOrUnbindService();//retry bind.
        notifyServiceConnectionChange(false);
    }
    /////////////////////////////////////////////////////////////////////////
    private ConnectServiceListener mListener;
    public void setConnectServiceListener(ConnectServiceListener listener){
        mListener = listener;
    }
    public interface ConnectServiceListener {
        void onServiceConnectionChange(boolean bind);
    }
    private synchronized void notifyServiceConnectionChange(boolean bind){
        if(mListener != null){
            mListener.onServiceConnectionChange(bind);
        }
    }
}
