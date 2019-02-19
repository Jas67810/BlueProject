package com.unre.ble.UI.weight;
//
// Created by JasWorkSpace on 2019/2/19.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.content.Context;
import android.support.v7.widget.SwitchCompat;
import android.util.AttributeSet;

public class BleSwitchButton extends SwitchCompat {
    public BleSwitchButton(Context context) {
        super(context);
    }
    public BleSwitchButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public BleSwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //ClientManager.getClient().registerBluetoothBondListener();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
    ///////////////////////////////////


}
