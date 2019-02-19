package com.unre.ble.UnBle.Client;
//
// Created by JasWorkSpace on 2019/2/18.
// Copyright (c) 2019 BlueProject All rights reserved.

public enum BleCharacterState {
    STATE_RELEASE(-1),
    STATE_IDLE(0),
    STATE_CONNECTING(1),
    STATE_CONNECTED(2),
    STATE_DISCONNECTED(3);
    ////////////////
    private final int state;
    BleCharacterState(int state){
        this.state = state;
    }
    public int getStateInt(){
        return state;
    }
    public static String tranformStateToString(BleCharacterState state){
        switch (state){
            case STATE_IDLE:return "STATE_IDLE";
            case STATE_CONNECTING:return "STATE_CONNECTING";
            case STATE_CONNECTED:return "STATE_CONNECTED";
            case STATE_DISCONNECTED:return "STATE_DISCONNECTED";
            case STATE_RELEASE:return "STATE_RELEASE";
            default:return ("STATE_" + state);
        }
    }
}
