package com.unre.unreble.Common;
//
// Created by JasWorkSpace on 2019/2/16.
// Copyright (c) 2019 BlueProject All rights reserved.

public interface UnBleListener {
    void onBleStateChangeListener(UnBleState oldState, UnBleState newState);
}
