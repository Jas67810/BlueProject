package com.unre.unreble.Service;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

public interface UnBleServiceListener {
    void onCharacteristicWrite(int offset, byte[] value);
    void onRegisteredDevicesChange();
    void onAdvertiseStateChange(boolean enable);
}
