package com.unre.unreble.Common;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.bluetooth.BluetoothAdapter;
import android.content.IntentFilter;

public abstract class IUnBleListener {
    private UnBleListener unBleListener;
    public void setUnBleListener(UnBleListener listener){
        unBleListener = listener;
    }
    ////////////////////////////////////////////////
    private volatile UnBleState mBleCurrentState = UnBleState.STATE_IDLE;
    private synchronized void notifyBleStateChange(UnBleState oldState, UnBleState newState){
        if(unBleListener != null){
            unBleListener.onBleStateChangeListener(oldState, newState);
        }
    }
    protected synchronized void setBleCurrentState(UnBleState state){
        if(mBleCurrentState != state){
            UnBleState old = mBleCurrentState;
            mBleCurrentState = state;
            notifyBleStateChange(mBleCurrentState, mBleCurrentState);
        }
    }
    public UnBleState getBleCurrentState(){
        return mBleCurrentState;
    }
    //////////////////////////////////////////////////////////////////////
}
