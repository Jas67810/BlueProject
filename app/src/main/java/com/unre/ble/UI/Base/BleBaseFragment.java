package com.unre.ble.UI.Base;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.qmuiteam.qmui.util.QMUIDisplayHelper;
import com.unre.ble.EventBus.EventUtils;
import com.unre.ble.EventBus.UnUseEvent;
import com.unre.ble.UnBle.BaseServiceHelper;
import com.unre.ble.UnBle.BleGatterService;
import com.unre.ble.UnBle.BleGatterServiceHelper;
import com.unre.ble.UnBle.Client.BleClientService;
import com.unre.ble.UnBle.Client.BleClientServiceHelper;
import com.unre.unreface.Base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Unbinder;

public abstract class BleBaseFragment extends BaseFragment implements BaseServiceHelper.ConnectServiceListener {
    private final static String TAG = BleBaseFragment.class.getSimpleName();
    public Unbinder mUnbinder;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        //mBleGatterServiceHelper = new BleGatterServiceHelper(getContext());
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unBinder();
    }
    public synchronized void unBinder(){
        if(mUnbinder != null){
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        EventUtils.register(false, this);
        if(mBleClientServiceHelper != null){
            mBleClientServiceHelper.bindService();//make sure its bind
        }
        if(mBleGatterServiceHelper != null){
            mBleGatterServiceHelper.bindService();//make sure its bind
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mBleClientServiceHelper != null){
            mBleClientServiceHelper.unBindService();
            mBleClientServiceHelper = null;
        }
        if(mBleGatterServiceHelper != null){
            mBleGatterServiceHelper.unBindService();
            mBleGatterServiceHelper = null;
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        EventUtils.unregister(false, this);
    }
    @Override
    public void onStart() {
        super.onStart();
        EventUtils.register(true, this);
    }
    @Override
    public void onStop() {
        super.onStop();
        EventUtils.unregister(true, this);
    }
    @Override
    protected int backViewInitOffset() {
        return QMUIDisplayHelper.dp2px(getContext(), 100);
    }
    @Subscribe
    public void unUseSubscribe(UnUseEvent event){}
    /////////////////////////////////////////
    public BleGatterServiceHelper mBleGatterServiceHelper;
    public BleClientServiceHelper mBleClientServiceHelper;
    private Object mObject = new Object();
    public BleGatterService getBleGatterService(){
        //maybe null, but no check
        return mBleGatterServiceHelper.getBleGatterService();
    }
    public BleClientService getBleClientService(){
        //maybe null, but no check
        return mBleClientServiceHelper.getBleClientService();
    }
    public synchronized void setupBleGatterService(){
        if(mBleGatterServiceHelper == null){
            mBleGatterServiceHelper = new BleGatterServiceHelper(getContext());
            mBleGatterServiceHelper.setConnectServiceListener(this);
        }
        mBleGatterServiceHelper.bindService();
    }
    public synchronized void setupBleClentService(){
        if(mBleClientServiceHelper == null){
            mBleClientServiceHelper = new BleClientServiceHelper(getContext());
            mBleClientServiceHelper.setConnectServiceListener(this);
        }
        mBleClientServiceHelper.bindService();
    }
    @Override
    public void onServiceConnectionChange(boolean bind) {
        Log.d(TAG, "onServiceConnectionChange:" + bind);
    }
}
