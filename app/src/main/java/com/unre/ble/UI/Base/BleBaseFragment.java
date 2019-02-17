package com.unre.ble.UI.Base;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.unre.ble.EventBus.Event;
import com.unre.ble.EventBus.EventUtils;
import com.unre.ble.EventBus.UnUseEvent;
import com.unre.ble.UnBle.BleGatterService;
import com.unre.ble.UnBle.BleGatterServiceHelper;
import com.unre.unreface.Base.BaseFragment;

import org.greenrobot.eventbus.Subscribe;

import butterknife.Unbinder;

public abstract class BleBaseFragment extends BaseFragment {
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
    @Subscribe
    public void unUseSubscribe(UnUseEvent event){}
    /////////////////////////////////////////
    public BleGatterServiceHelper mBleGatterServiceHelper;

}
