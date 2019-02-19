package com.unre.ble.UI.Base;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.unre.ble.EventBus.EventUtils;
import com.unre.ble.EventBus.UnUseEvent;
import com.unre.ble.R;
import com.unre.ble.UI.Permission.IPermissionListenerWrap;
import com.unre.ble.UI.Permission.Permission;
import com.unre.ble.UI.Permission.PermissionsHelper;
import com.unre.unreface.Base.BaseFragmentActivity;

import org.greenrobot.eventbus.Subscribe;

public class BleBaseFragmentActivity extends BaseFragmentActivity implements IPermissionListenerWrap.IEachPermissionListener{
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
    //////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 10:{
                onSettingsPermissionsResult();
            }break;
        }
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
            Toast.makeText(BleBaseFragmentActivity.this, getResources().getString(R.string.permission, permission.name), Toast.LENGTH_SHORT).show();
            if (!permission.shouldShowRequestPermissionRationale) {
                PermissionsHelper.startSettingActivity(BleBaseFragmentActivity.this);
            }
            finish();
        }
    }
    @Override
    public void onException(Throwable throwable) {

    }
    public void onSettingsPermissionsResult(){

    }
}
