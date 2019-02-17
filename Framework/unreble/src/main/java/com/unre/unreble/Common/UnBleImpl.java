package com.unre.unreble.Common;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import static android.content.Context.BLUETOOTH_SERVICE;

public class UnBleImpl extends IUnBleListener implements IUnBle{
    public Context mContext;
    public BluetoothManager mBluetoothManager;
    public UnBleImpl(Context context){
        mContext = context.getApplicationContext();
        mBluetoothManager = (BluetoothManager) mContext.getSystemService(BLUETOOTH_SERVICE);
    }
    //////////////////////////////////////////////////////
    // API
    protected boolean canSteup(){
        UnBleState state = getBleCurrentState();
        return (state == UnBleState.STATE_IDLE || state == UnBleState.STATE_CLOSED);
    }
    @Override
    public synchronized boolean setup() {
        if(canSteup()){
            if(UnBleUtils.checkBluetoothSupport(mContext)){
                registerBleReceiver();
                return true;
            }
        }
        return false;
    }
    @Override
    public synchronized boolean teardown() {
        unregisterBleReceiver();
        return true;
    }
    /////////////////////////////////////////////////
    private BluetoothReceiver mBluetoothReceiver = null;
    private void registerBleReceiver(){
        if(mBluetoothReceiver == null) {
            IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
            mContext.registerReceiver(mBluetoothReceiver = new BluetoothReceiver(), filter);
        }
    }
    private void unregisterBleReceiver(){
        if(mBluetoothReceiver != null){
            mContext.unregisterReceiver(mBluetoothReceiver);
            mBluetoothReceiver = null;
        }
    }
    private class BluetoothReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(intent.getAction())) {
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.STATE_OFF);
                onBluetoothAdapterStateChange(state);
            }
        }
    }

    public void onBluetoothAdapterStateChange(int state){
        //donothing here.
    }
    ///////////////////////////////////////
    public boolean isBleEnable(){
        return mBluetoothManager.getAdapter().isEnabled();
    }
    public boolean enbaleBle(){
        return mBluetoothManager.getAdapter().enable();
    }
    public boolean disableBle(){
        return mBluetoothManager.getAdapter().disable();
    }

}
