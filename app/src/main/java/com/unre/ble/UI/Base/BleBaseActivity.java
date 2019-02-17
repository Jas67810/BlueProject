package com.unre.ble.UI.Base;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.widget.Toast;

import com.unre.ble.EventBus.Event;
import com.unre.ble.EventBus.EventBusHelper;
import com.unre.ble.EventBus.EventUtils;
import com.unre.ble.EventBus.UnUseEvent;
import com.unre.ble.R;
import com.unre.ble.UI.Permission.IPermissionListenerWrap;
import com.unre.ble.UI.Permission.Permission;
import com.unre.ble.UI.Permission.PermissionsHelper;
import com.unre.unreface.Base.BaseActivity;

import org.greenrobot.eventbus.Subscribe;

public class BleBaseActivity extends BaseActivity implements IPermissionListenerWrap.IEachPermissionListener{
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

    ////////////////////////////////////////////////////////////////////
    /// 获取权限
    private volatile PermissionsHelper mPermissionsHelper;
    public PermissionsHelper getPermissionsHelper() {
        if (mPermissionsHelper == null) {
            synchronized (BleBaseFragmentActivity.class) {
                if (mPermissionsHelper == null) {
                    mPermissionsHelper = new PermissionsHelper(this);
                }
            }
        }
        return mPermissionsHelper;
    }
    public void requestPermission(final String[] permissions) {
        getPermissionsHelper().requestEachPermissions(permissions, this);
    }
    @Override
    public void onAccepted(Permission permission) {
        if(!permission.granted){
            Toast.makeText(this, getResources().getString(R.string.permission, permission.name), Toast.LENGTH_SHORT).show();
            if (!permission.shouldShowRequestPermissionRationale) {
                PermissionsHelper.startSettingActivity(this);
            }
            finish();
        }
    }
    @Override
    public void onException(Throwable throwable) {

    }
    @Subscribe
    public void unUseSubscribe(UnUseEvent event){}
}
