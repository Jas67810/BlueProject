package com.unre.ble.UI.activity;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

import android.Manifest;
import android.os.Bundle;

import com.unre.ble.UI.Base.BleBaseFragment;
import com.unre.ble.UI.Base.BleBaseFragmentActivity;
import com.unre.ble.UI.fragment.FragmentPeriphera;

public class PeripheraActivity extends BleBaseFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //startFragment(new FragmentCentral());
        if (savedInstanceState == null) {
            BleBaseFragment fragment = new FragmentPeriphera();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(getContextViewId(), fragment, fragment.getClass().getSimpleName())
                    .addToBackStack(fragment.getClass().getSimpleName())
                    .commit();
        }
        requestPermission();
    }
    //////////////////////////////////////////
    private void requestPermission(){
        requestPermission(new String[]{
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION
        });
    }
    @Override
    public void onSettingsPermissionsResult() {
        super.onSettingsPermissionsResult();
        requestPermission();
    }
}
