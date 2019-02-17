package com.unre.ble;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.app.Application;

import com.qmuiteam.qmui.arch.QMUISwipeBackActivityManager;
import com.squareup.leakcanary.LeakCanary;
import com.unre.ble.Utils.OSUtils;

public class BleApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);//所有进程添加内存检查。
        if(OSUtils.isInPackageProcess(this)){
            QMUISwipeBackActivityManager.init(this);
        }
    }
}
